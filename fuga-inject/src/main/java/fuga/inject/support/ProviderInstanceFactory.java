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
import fuga.inject.Provider;
import fuga.util.ObjectUtils;

class ProviderInstanceFactory<T> implements InternalFactory<T> {

    private final Provider<T> provider;

    ProviderInstanceFactory(Provider<T> provider) {
        this.provider = provider;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        try {
            var instance = provider.get();

            if (instance == null && !dependency.isNullable()) {
                throw InternalProvisionException.nullInjectedIntoNonNullableDependency(
                        context.getDependency().getKey().getRawType(), dependency);
            }

            return instance;
        } catch (RuntimeException e) {
            throw InternalProvisionException.errorInProvider(e);
        }
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("provider", provider)
                .toString();
    }
}