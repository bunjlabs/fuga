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

import java.util.Collection;
import java.util.HashSet;

class MultiDependencyInjector<T> implements DependencyInjector<T> {
    private final Dependency<?> dependency;
    private final Collection<InternalFactory<?>> internalFactories;

    MultiDependencyInjector(Dependency<?> dependency, Collection<InternalFactory<?>> internalFactories) {
        this.dependency = dependency;
        this.internalFactories = internalFactories;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T inject(InjectorContext context) throws InternalProvisionException {
        context.pushDependency(dependency);
        try {
            var dependencySet = new HashSet<>();
            for(InternalFactory<?> factory : internalFactories) {
                dependencySet.add(factory.get(context, dependency));
            }

            // T is actually a Set or Collection
            return (T) dependencySet;
        } finally {
            context.popDependency();
        }
    }
}
