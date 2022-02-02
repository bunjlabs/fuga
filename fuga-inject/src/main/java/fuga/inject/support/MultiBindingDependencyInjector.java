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

import fuga.inject.Dependency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

class MultiBindingDependencyInjector<T> implements DependencyInjector<T> {
    private final Dependency<?> dependency;
    private final Collection<AbstractBinding<?>> bindings;
    private final Collection<InternalFactory<?>> internalFactories;

    MultiBindingDependencyInjector(Dependency<?> dependency, Collection<AbstractBinding<?>> bindings) {
        this.dependency = dependency;
        this.bindings = bindings;
        this.internalFactories = new ArrayList<>(bindings.size());
        bindings.forEach(b ->  this.internalFactories.add(b.getInternalFactory()));
    }

    @Override
    public T inject(InjectorContext context) throws InternalProvisionException {
        context.pushDependency(dependency);
        try {
            return createDependencySet(context);
        } finally {
            context.popDependency();
        }
    }

    @SuppressWarnings("unchecked")
    private T createDependencySet(InjectorContext context) throws InternalProvisionException {
        var dependencySet = new HashSet<>();

        var bindingsIterator = bindings.iterator();
        var factoriesIterator = internalFactories.iterator();
        while(bindingsIterator.hasNext() && factoriesIterator.hasNext()) {
            var binding = bindingsIterator.next();
            var factory = factoriesIterator.next();

            context.pushBinding(binding);
            try {
                dependencySet.add(factory.get(context));
            } finally {
                context.popBinding();
            }
        }
        // T is actually a Set or Collection
        return (T) dependencySet;
    }
}
