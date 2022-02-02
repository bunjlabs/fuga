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

import fuga.common.Key;
import fuga.common.errors.ErrorMessages;
import fuga.inject.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class InternalInjectorBuilder {

    private Iterable<? extends Unit> units = Collections.emptyList();
    private InjectorImpl parent = null;

    public InternalInjectorBuilder withUnits(Iterable<? extends Unit> units) {
        this.units = units;
        return this;
    }

    public InternalInjectorBuilder withParent(InjectorImpl parentInjector) {
        this.parent = parentInjector;
        return this;
    }

    public Injector build() {
        var errorMessages = new ErrorMessages();

        if (parent == null) {
            parent = createRootInjector(errorMessages);
        }

        var container = new InheritedContainer(parent.getContainer());
        var context = new SetupContext(container, errorMessages);
        context.pushResolverInjector(parent);

        var injectorUnit = new InjectorUnit();
        context.bindingProcessor.scheduleInitialization(injectorUnit.adapter);

        setupUnits(context, injectorUnit);
        setupUnits(context, units);

        var injector = new InjectorImpl(parent, container);
        context.bindingProcessor.initialize(injector);

        if (errorMessages.hasErrors()) {
            throw new ConfigurationException(errorMessages.formatMessages());
        }

        return injector;
    }

    private void setupUnits(SetupContext context, Unit... units) {
        setupUnits(context, Arrays.asList(units));
    }

    private void setupUnits(SetupContext context, Iterable<? extends Unit> units) {
        var configuration = new DefaultConfiguration();

        for (var unit : units) {
            try {
                unit.setup(configuration);
            } catch (RuntimeException e) {
                throw new ConfigurationException("Unable to setup unit " + unit, e);
            }
        }

        if (!configuration.getInstalledUnits().isEmpty()) {
            setupUnits(context, configuration.getInstalledUnits());
        }

        configuration.getInstalledUnits().forEach(u -> context.satisfiedUnits.add(Key.of(u.getClass())));
        configuration.getRequiredUnits().removeIf(context.satisfiedUnits::contains);

        if (!configuration.getRequiredUnits().isEmpty()) {
            var resolverUnit = (Unit) c -> configuration.getRequiredUnits().forEach(c::bind);
            var resolverInjector = context.pushResolverInjector(resolverUnit);
            if (resolverInjector != null) {
                var resolvedUnits = configuration.getRequiredUnits().stream()
                        .map(resolverInjector::getInstance)
                        .collect(Collectors.toUnmodifiableList());

                configuration.getRequiredUnits().clear();
                setupUnits(context, resolvedUnits);
                context.satisfiedUnits.addAll(configuration.getRequiredUnits());

                context.popResolverInjector();
            }
        }

        configuration.getAttachments()
                .forEach(context.container::putAttachment);

        configuration.getEncounters()
                .forEach(context.container::putEncounter);

        configuration.getWatchings()
                .forEach(context.container::putWatching);

        configuration.getBindings().stream()
                .filter(context.bindingProcessor::process)
                .forEachOrdered(context.container::putBinding);
    }

    private InjectorImpl createRootInjector(ErrorMessages errorMessages) {
        var context = new SetupContext(errorMessages);

        setupUnits(context, new RootUnit());

        return new InjectorImpl(null, context.container);
    }

    private static class RootUnit implements Unit {

        @Override
        public void setup(Configuration c) {
            c.bind(SingletonScope.class).toInstance(new SingletonScope());
        }
    }

    private static class InjectorUnit implements Unit {

        private final ProviderToInternalFactoryAdapter<Injector> adapter = new ProviderToInternalFactoryAdapter<>(new InjectorFactory());

        @Override
        public void setup(Configuration c) {
            c.bind(Injector.class).toProvider(adapter);
        }
    }

    private static class InjectorFactory implements InternalFactory<Injector> {
        @Override
        public Injector get(InjectorContext context) throws InternalProvisionException {
            return context.getInjector();
        }
    }

    private static class SetupContext {
        final Container container;

        final Deque<Injector> resolverInjectors = new ConcurrentLinkedDeque<>();
        final Set<Key<? extends Unit>> satisfiedUnits = new HashSet<>();

        final AbstractBindingProcessor bindingProcessor;

        SetupContext(ErrorMessages errorMessages) {
            this(new InheritedContainer(Container.EMPTY), errorMessages);
        }

        SetupContext(Container container, ErrorMessages errorMessages) {
            this.container = container;
            this.bindingProcessor = new DefaultBindingProcessor(container, errorMessages);
        }

        Injector pushResolverInjector(Unit u) {
            var baseInjector = resolverInjectors.peek();
            if (baseInjector == null) {
                return null;
            }

            return pushResolverInjector(baseInjector.createChildInjector(u));
        }

        Injector pushResolverInjector(Injector injector) {
            resolverInjectors.push(injector);
            return injector;
        }

        void popResolverInjector() {
            resolverInjectors.pop();
        }
    }
}
