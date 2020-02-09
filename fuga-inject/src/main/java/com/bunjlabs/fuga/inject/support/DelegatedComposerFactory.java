/*
 * Copyright 2019 Bunjlabs
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

import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Dependency;
import com.bunjlabs.fuga.inject.Key;

class DelegatedComposerFactory<T> implements InternalFactory<T> {

    private final Key<? extends Composer> composerKey;

    DelegatedComposerFactory(Key<? extends Composer> composerKey) {
        this.composerKey = composerKey;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        var composer = context.getInjector().getInstance(composerKey);

        try {
            T instance = getFromComposer(composer, context.getDependency().getKey(), dependency.getKey());

            if (instance == null && !dependency.isNullable()) {
                throw InternalProvisionException.nullInjectedIntoNonNullableDependency(
                        context.getDependency().getKey().getRawType(), dependency);
            }

            return instance;
        } catch (RuntimeException e) {
            throw InternalProvisionException.errorInComposer(e);
        }
    }

    @SuppressWarnings("unchecked")
    private T getFromComposer(Composer composer, Key<?> requester, Key<?> requested) {
        return (T) composer.get(requester, requested);
    }
}
