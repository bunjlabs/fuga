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

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Key;

abstract class AbstractBinding<T> implements Binding<T> {

    private final Key<T> key;
    private final InternalFactory<T> internalFactory;
    private final Scoping scoping;

    AbstractBinding(Key<T> key, Scoping scoping) {
        this.key = key;
        this.scoping = scoping;
        this.internalFactory = null;
    }

    AbstractBinding(Key<T> key, InternalFactory<T> internalFactory) {
        this.key = key;
        this.scoping = null;
        this.internalFactory = internalFactory;
    }

    @Override
    public Key<T> getKey() {
        return key;
    }

    public Scoping getScoping() {
        return scoping;
    }

    protected abstract AbstractBinding<T> withScoping(Scoping scoping);

    InternalFactory<T> getInternalFactory() {
        return internalFactory;
    }
}
