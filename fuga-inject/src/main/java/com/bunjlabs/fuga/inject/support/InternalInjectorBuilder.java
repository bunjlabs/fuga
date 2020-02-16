/*
 * Copyright 2019-2020 Bunjlabs
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

package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.common.errors.ErrorMessages;
import com.bunjlabs.fuga.inject.*;

import java.util.LinkedList;
import java.util.List;

public class InternalInjectorBuilder {

    private final List<Unit> units = new LinkedList<>();
    private InjectorImpl parent = null;

    public InternalInjectorBuilder withUnits(Iterable<Unit> units) {
        units.forEach(this.units::add);
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

        var injectorUnit = new InjectorUnit();
        bindingProcessor.scheduleInitialization(injectorUnit.adapter);
        setupUnit(injectorUnit, bindingProcessor, scopeBindingProcessor);

        for (var unit : units) {
            setupUnit(unit, bindingProcessor, scopeBindingProcessor);
        }

        var injector = new InjectorImpl(parent, container);
        bindingProcessor.getUninitialized().forEach(i -> i.initialize(injector));

        if (errorMessages.hasErrors()) {
            throw new ConfigurationException(errorMessages.formatMessages());
        }

        return injector;
    }

    private void setupUnit(Unit unit, AbstractBindingProcessor bindingProcessor, ScopeBindingProcessor scopeBindingProcessor) {
        var configuration = new DefaultConfiguration();

        try {
            unit.setup(configuration);
        } catch (RuntimeException e) {
            throw new ConfigurationException("Unable to setup unit " + unit, e);
        }

        for (var innerUnit : configuration.getInstalledUnits()) {
            setupUnit(innerUnit, bindingProcessor, scopeBindingProcessor);
        }

        for (AbstractBinding<?> binding : configuration.getBindings()) {
            bindingProcessor.process(binding);
        }

        for (ScopeBinding scopeBinding : configuration.getScopeBindings()) {
            scopeBindingProcessor.process(scopeBinding);
        }
    }

    private InjectorImpl createRootInjector(ErrorMessages errorMessages) {
        var container = new InheritedContainer(Container.EMPTY);
        var bindingProcessor = new DefaultBindingProcessor(container, errorMessages);
        var scopeBindingProcessor = new DefaultScopeBindingProcessor(container, errorMessages);

        setupUnit(new RootUnit(), bindingProcessor, scopeBindingProcessor);

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
}
