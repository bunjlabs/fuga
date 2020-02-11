/*
 * Copyright 2019 Bunjlabs
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

import com.bunjlabs.fuga.inject.*;

import java.util.*;

class InjectorImpl implements Injector {

    private final Injector parent;
    private final Container container;
    private InjectorContext context;

    InjectorImpl(Injector parent, Container container) {
        this.parent = parent;
        this.container = container;
    }

    <T> InternalFactory<T> getInternalFactory(Key<T> key) {
        return getBinding(key).getInternalFactory();
    }

    <T> Collection<InternalFactory<T>> getInternalFactories(Key<T> key) {
        var bindingList = container.getAllExplicitBindings(key);
        var factories = new ArrayList<InternalFactory<T>>(bindingList.size());
        bindingList.forEach(b -> factories.add(b.getInternalFactory()));

        return factories;
    }

    <T> DependencyInjector<T> getDependencyInjector(Dependency<T> dependency) {
        if (dependency.isRequestedAll()) {
            if (!dependency.getKey().getRawType().isAssignableFrom(Set.class)) {
                throw new ProvisionException("Not a collection" + dependency);
            }

            // T is actually a Collection
            var realKey = dependency.getRealKey();
            var realDependency = Dependency.of(realKey);

            var bindingList = container.getAllExplicitBindings(realKey);
            var factories = new ArrayList<InternalFactory<?>>(bindingList.size());
            bindingList.forEach(b -> factories.add(b.getInternalFactory()));

            return new MultiDependencyInjector<>(realDependency, factories);
        } else {
            return new SingleDependencyInjector<>(dependency, getInternalFactory(dependency.getKey()));
        }
    }

    Container getContainer() {
        return container;
    }

    InjectorContext getContext() {
        if (context == null) {
            context = new InjectorContext(this);
        }

        return context;
    }

    @Override
    public Injector getParent() {
        return parent;
    }

    @Override
    public Injector createChildInjector(Unit... units) {
        return createChildInjector(Arrays.asList(units));
    }

    @Override
    public Injector createChildInjector(Iterable<Unit> units) {
        return new InternalInjectorBuilder().withParent(this).withUnits(units).build();
    }

    @Override
    public <T> AbstractBinding<T> getBinding(Class<T> type) {
        return getBinding(Key.of(type));
    }

    @Override
    public <T> AbstractBinding<T> getBinding(Key<T> key) {
        var binding = container.getExplicitBinding(key);

        if (binding != null) {
            return binding;
        }

        throw new ProvisionException("No binding available for " + key);
    }

    @Override
    public <T> Set<Binding<T>> getAllBindings(Class<T> type) {
        return getAllBindings(Key.of(type));
    }

    @Override
    public <T> Set<Binding<T>> getAllBindings(Key<T> key) {
        var bindingList = container.getAllExplicitBindings(key);
        return new HashSet<>(bindingList);
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        return getProvider(Key.of(type));
    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        var binding = getBinding(key);
        var internalFactory = binding.getInternalFactory();

        return () -> doGetInstance(key, internalFactory);
    }

    @Override
    public <T> Set<Provider<T>> getAllProviders(Class<T> type) {
        return getAllProviders(Key.of(type));
    }

    @Override
    public <T> Set<Provider<T>> getAllProviders(Key<T> key) {
        var internalFactories = getInternalFactories(key);
        var providers = new HashSet<Provider<T>>();

        internalFactories.forEach(internalFactory -> providers.add(() -> doGetInstance(key, internalFactory)));

        return providers;
    }

    @Override
    public <T> T getInstance(Class<T> type) {
        return getInstance(Key.of(type));
    }

    @Override
    public <T> T getInstance(Key<T> key) {
        return getProvider(key).get();
    }

    @Override
    public <T> Set<T> getAllInstances(Class<T> type) {
        return getAllInstances(Key.of(type));
    }

    @Override
    public <T> Set<T> getAllInstances(Key<T> key) {
        var providers = getAllProviders(key);
        var instances = new HashSet<T>();

        for (var provider : providers) {
            instances.add(provider.get());
        }

        return instances;
    }

    private <T> T doGetInstance(Key<T> key, InternalFactory<T> internalFactory) {
        var dependency = Dependency.of(key);
        var localContext = getContext();
        localContext.pushDependency(dependency);
        try {
            return internalFactory.get(localContext, dependency);
        } catch (InternalProvisionException e) {
            throw e.toProvisionException();
        } finally {
            localContext.popDependency();
        }
    }
}
