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

import com.bunjlabs.fuga.inject.Provider;

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
        var ctx = injector.getContext();
        var dependency = ctx.getDependency();

        try {
            return internalFactory.get(ctx, dependency);
        } catch (InternalProvisionException e) {
            throw e.toProvisionException();
        }
    }

    @Override
    public void initialize(InjectorImpl injector) {
        this.injector = injector;
    }
}
