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

class ProviderToInternalFactoryAdapter<T> implements Provider<T>, Initializable {

    private final InternalFactory<T> internalFactory;
    private InjectorImpl injector;

    ProviderToInternalFactoryAdapter(InternalFactory<T> internalFactory) {
        this.internalFactory = internalFactory;
    }

    @Override
    public T get() {
        if (injector == null) {
            throw new IllegalStateException("Injector is not ready");
        }

        var context = injector.getContext();

        try {
            return internalFactory.get(context);
        } catch (InternalProvisionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(InjectorImpl injector) {
        this.injector = injector;
    }
}
