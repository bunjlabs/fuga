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
import fuga.inject.Provider;

class DelegatedProviderFactory<T> implements InternalFactory<T>, Initializable {

    private final Key<? extends Provider<? extends T>> providerKey;
    private DependencyInjector<? extends Provider<? extends T>> providerInjector;

    DelegatedProviderFactory(Key<? extends Provider<? extends T>> providerKey) {
        this.providerKey = providerKey;
    }

    @Override
    public void initialize(InjectorImpl injector) {
        var dependency = Dependency.of(providerKey);

        providerInjector = injector.getDependencyInjector(dependency);
        if (providerInjector == null) {
            throw new ConfigurationException("Unsatisfied provider dependency: " + dependency);
        }
    }

    @Override
    public T get(InjectorContext context) throws InternalProvisionException {
        var provider = providerInjector.inject(context);

        try {
            return provider.get();
        } catch (RuntimeException e) {
            throw InternalProvisionException.errorInProvider(e);
        }
    }
}
