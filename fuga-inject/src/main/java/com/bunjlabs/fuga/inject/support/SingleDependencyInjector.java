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

import com.bunjlabs.fuga.inject.Dependency;

class SingleDependencyInjector<T> implements DependencyInjector<T> {

    private final Dependency<T> dependency;
    private final InternalFactory<T> internalFactory;

    public SingleDependencyInjector(Dependency<T> dependency, InternalFactory<T> internalFactory) {
        this.dependency = dependency;
        this.internalFactory = internalFactory;
    }

    @Override
    public T inject(InjectorContext context) throws InternalProvisionException {
        context.pushDependency(dependency);
        try {
            return internalFactory.get(context, dependency);
        } finally {
            context.popDependency();
        }
    }
}
