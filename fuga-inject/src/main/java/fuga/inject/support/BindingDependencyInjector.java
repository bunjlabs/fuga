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

class BindingDependencyInjector<T> implements DependencyInjector<T> {

    private final Dependency<T> dependency;
    private final AbstractBinding<T> binding;
    private final InternalFactory<T> internalFactory;

    BindingDependencyInjector(Dependency<T> dependency, AbstractBinding<T> binding) {
        this.dependency = dependency;
        this.binding = binding;
        this.internalFactory = binding.getInternalFactory();
    }

    @Override
    public T inject(InjectorContext context) throws InternalProvisionException {
        context.pushDependency(dependency);
        context.pushBinding(binding);
        try {
            var instance = internalFactory.get(context);

            if (instance == null && !dependency.isNullable()) {
                throw InternalProvisionException.nullInjectedIntoNonNullableDependency(context.getRequester().getType(), dependency);
            }

            return instance;
        } finally {
            context.popBinding();
            context.popDependency();
        }
    }
}
