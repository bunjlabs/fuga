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

import java.util.Arrays;

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

    <T> DependencyInjector<T> getDependencyInjector(Dependency<T> dependency) {
        return new DependencyInjector<>(dependency, getInternalFactory(dependency.getKey()));
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

        throw new ProvisionException("No binding found for " + key);
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        return getProvider(Key.of(type));
    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        var binding = getBinding(key);
        var internalFactory = binding.getInternalFactory();

        return () -> {
            var dependency = Dependency.of(binding.getKey());
            var localContext = getContext();
            localContext.pushDependency(dependency);
            try {
                return internalFactory.get(localContext, Dependency.of(binding.getKey()));
            } catch (InternalProvisionException e) {
                throw e.toProvisionException();
            } finally {
                localContext.popDependency();
            }
        };
    }

    @Override
    public <T> T getInstance(Class<T> type) {
        return getInstance(Key.of(type));
    }

    @Override
    public <T> T getInstance(Key<T> key) {
        return getProvider(key).get();
    }

}
