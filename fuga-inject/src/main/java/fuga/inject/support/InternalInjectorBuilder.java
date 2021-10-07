/*
 * Copyright 2019-2021 Bunjlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fuga.inject.support;

import fuga.common.errors.ErrorMessages;
import fuga.inject.*;

import java.util.Collections;

public class InternalInjectorBuilder {

    private Iterable<Unit> units = Collections.emptyList();
    private InjectorImpl parent = null;

    public InternalInjectorBuilder withUnits(Iterable<Unit> units) {
        this.units = units;
        return this;
    }

    public InternalInjectorBuilder withParent(InjectorImpl injector) {
        this.parent = injector;
        return this;
    }

    public Injector build() {
        var errorMessages = new ErrorMessages();

        if (parent == null) {
            parent = createRootInjector(errorMessages);
        }

        var container = new InheritedContainer(parent.getContainer());
        var bindingProcessor = new DefaultBindingProcessor(container, errorMessages);
        var scopeBindingProcessor = new DefaultScopeBindingProcessor(container, errorMessages);
        var listenerBindingProcessor = new DefaultWatchingProcessor(container, errorMessages);

        var context = new SetupContext(bindingProcessor, scopeBindingProcessor, listenerBindingProcessor);

        var injectorUnit = new InjectorUnit();
        bindingProcessor.scheduleInitialization(injectorUnit.adapter);

        setupUnit(injectorUnit, context);

        units.forEach(u -> setupUnit(u, context));

        var injector = new InjectorImpl(parent, container);
        bindingProcessor.getUninitialized().forEach(i -> i.initialize(injector));

        if (errorMessages.hasErrors()) {
            throw new ConfigurationException(errorMessages.formatMessages());
        }

        return injector;
    }

    private void setupUnit(Unit unit, SetupContext context) {
        var configuration = new DefaultConfiguration();

        try {
            unit.setup(configuration);
        } catch (RuntimeException e) {
            throw new ConfigurationException("Unable to setup unit " + unit, e);
        }

        configuration.getInstalledUnits().forEach(u -> setupUnit(u, context));

        configuration.getScopeBindings().removeIf(context.scopeBindingProcessor::process);
        configuration.getKeyedWatchings().removeIf(context.watchingsProcessor::process);
        configuration.getMatchedWatchings().removeIf(context.watchingsProcessor::process);
        configuration.getBindings().removeIf(context.bindingProcessor::process);
    }

    private InjectorImpl createRootInjector(ErrorMessages errorMessages) {
        var container = new InheritedContainer(Container.EMPTY);
        var bindingProcessor = new DefaultBindingProcessor(container, errorMessages);
        var scopeBindingProcessor = new DefaultScopeBindingProcessor(container, errorMessages);
        var listenerBindingProcessor = new DefaultWatchingProcessor(container, errorMessages);

        var context = new SetupContext(bindingProcessor, scopeBindingProcessor, listenerBindingProcessor);
        setupUnit(new RootUnit(), context);

        return new InjectorImpl(null, container);
    }

    private static class RootUnit implements Unit {

        @Override
        public void setup(Configuration c) {
            c.bindScope(Singleton.class, new SingletonScope());
        }
    }

    private static class InjectorUnit implements Unit {

        private final ProviderToInternalFactoryAdapter<Injector> adapter = new ProviderToInternalFactoryAdapter<>(new InjectorFactory());

        @Override
        public void setup(Configuration c) {
            c.bind(Injector.class).toProvider(adapter);
        }
    }

    private static class SetupContext {
        final BindingProcessor bindingProcessor;
        final ScopeBindingProcessor scopeBindingProcessor;
        final DefaultWatchingProcessor watchingsProcessor;

        public SetupContext(BindingProcessor bindingProcessor, ScopeBindingProcessor scopeBindingProcessor, DefaultWatchingProcessor watchingsProcessor) {
            this.bindingProcessor = bindingProcessor;
            this.scopeBindingProcessor = scopeBindingProcessor;
            this.watchingsProcessor = watchingsProcessor;
        }
    }
}
