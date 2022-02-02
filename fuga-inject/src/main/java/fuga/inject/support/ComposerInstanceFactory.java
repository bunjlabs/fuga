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
import fuga.inject.Composer;

class ComposerInstanceFactory<T> implements InternalFactory<T> {

    private final Key<T> targetKey;
    private final Composer proxiedComposer;

    ComposerInstanceFactory(Key<T> targetKey, Composer proxiedComposer) {
        this.targetKey = targetKey;
        this.proxiedComposer = proxiedComposer;
    }

    @Override
    public T get(InjectorContext context) throws InternalProvisionException {
        var requester = context.getRequester().getKey();
        try {
            var instance = getFromProxiedFactory(requester, targetKey);

            if (instance != null && !targetKey.getRawType().isAssignableFrom(instance.getClass())) {
                throw new ClassCastException("Composer returned unexpected type: "
                        + instance.getClass() +
                        ". Expected: "
                        + targetKey.getRawType());
            }

            return instance;
        } catch (RuntimeException e) {
            throw InternalProvisionException.errorInComposer(e);
        }
    }

    @SuppressWarnings("unchecked")
    private T getFromProxiedFactory(Key<?> requester, Key<?> requested) {
        return (T) proxiedComposer.get(requester, requested);
    }
}
