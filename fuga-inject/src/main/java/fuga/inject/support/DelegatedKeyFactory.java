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
import fuga.inject.ConfigurationException;
import fuga.inject.Dependency;

class DelegatedKeyFactory<T> implements InternalFactory<T>, Initializable {

    private final Key<? extends T> targetKey;
    private DependencyInjector<? extends T> targetInjector;

    DelegatedKeyFactory(Key<? extends T> targetKey) {
        this.targetKey = targetKey;
    }

    @Override
    public void initialize(InjectorImpl injector) {
        var dependency = Dependency.of(targetKey);

        targetInjector = injector.getDependencyInjector(dependency);
        if (targetInjector == null) {
            throw new ConfigurationException("Unsatisfied target dependency: " + dependency);
        }
    }

    @Override
    public T get(InjectorContext context) throws InternalProvisionException {
        return targetInjector.inject(context);
    }
}
