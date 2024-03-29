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

import fuga.inject.Provider;

class ProviderInstanceFactory<T> implements InternalFactory<T> {

    private final Provider<T> provider;

    ProviderInstanceFactory(Provider<T> provider) {
        this.provider = provider;
    }

    @Override
    public T get(InjectorContext context) throws InternalProvisionException {
        try {
            return provider.get();
        } catch (RuntimeException e) {
            if (e.getCause() instanceof InternalProvisionException) {
                throw (InternalProvisionException) e.getCause();
            }

            throw InternalProvisionException.errorInProvider(e);
        }
    }
}
