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
import fuga.inject.*;

import java.util.*;
import java.util.stream.Collectors;

class InjectorImpl implements Injector {

    private final Injector parent;
    private final Container container;
    private final ThreadLocal<InjectorContext> context = ThreadLocal.withInitial(() -> new InjectorContext(this));

    InjectorImpl(Injector parent, Container container) {
        this.parent = parent;
        this.container = container;
    }

    <T> DependencyInjector<T> getDependencyInjector(Dependency<T> dependency) {
        if (dependency.isTargetAttr()) {
            return new AttributeDependencyInjector<>(dependency);
        } else if (dependency.isTargetSource()) {
            return new SourceDependencyInjector<>(dependency);
        } else if (dependency.isRequestedAll()) {
            // T is actually a Collection
            var realKey = dependency.getRealKey();
            var realDependency = Dependency.of(realKey);
            var bindings = new ArrayList<AbstractBinding<?>>(container.getAllExplicitBindings(realKey));
            return new MultiBindingDependencyInjector<T>(realDependency, bindings);
        } else {
            var key = dependency.getKey();
            var localContext = getContext();
            var binding = localContext.findBinding(key);
            if (binding == null) {
                binding = getBinding(key);
            }

            return new BindingDependencyInjector<>(dependency, binding);
        }
    }

    <T> DependencyInjector<T> getBindingDependencyInjector(AbstractBinding<T> binding) {
        return new BindingDependencyInjector<>(Dependency.of(binding.getKey()), binding);
    }

    Container getContainer() {
        return container;
    }

    InjectorContext getContext() {
        return context.get();
    }

    @Override
    public Injector getParent() {
        return parent;
    }

    @Override
    public Injector createChildInjector(Iterable<? extends Unit> units) {
        return new InternalInjectorBuilder().withParent(this).withUnits(units).build();
    }

    @Override
    public <T> AbstractBinding<T> getBinding(Key<T> key) {
        var binding = container.getExplicitBinding(key);
        if (binding == null) {
            throw new ConfigurationException("No binding available for " + key);
        }

        return binding;
    }

    @Override
    public <T> Set<Binding<T>> getAllBindings(Key<T> key) {
        return new HashSet<>(container.getAllExplicitBindings(key));
    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        var binding = getBinding(key);
        return () -> doGetInstance(binding);
    }

    @Override
    public <T> Set<Provider<T>> getAllProviders(Key<T> key) {
        return container.getAllExplicitBindings(key).stream()
                .map(b -> (Provider<T>) () -> doGetInstance(b))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public <T> T getInstance(Key<T> key) {
        return doGetInstance(getBinding(key));
    }

    @Override
    public <T> Set<T> getAllInstances(Key<T> key) {
        return container.getAllExplicitBindings(key).stream()
                .map(this::doGetInstance)
                .collect(Collectors.toUnmodifiableSet());
    }

    private <T> T doGetInstance(AbstractBinding<T> binding) {
        var localContext = getContext();
        var dependencyInjector = new BindingDependencyInjector<>(Dependency.of(binding.getKey()), binding);

        try {
            return dependencyInjector.inject(localContext);
        } catch (InternalProvisionException e) {
            throw e.toProvisionException();
        }
    }
}
