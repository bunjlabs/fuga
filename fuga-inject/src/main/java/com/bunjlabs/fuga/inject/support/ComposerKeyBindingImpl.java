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

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.bindings.ComposerKeyBinding;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

class ComposerKeyBindingImpl<T> extends AbstractBinding<T> implements ComposerKeyBinding<T> {

    private final Key<? extends Composer> composerKey;

    ComposerKeyBindingImpl(Key<T> key, Scoping scoping, Key<? extends Composer> composerKey) {
        super(key, scoping);
        this.composerKey = composerKey;
    }

    ComposerKeyBindingImpl(Key<T> key, Key<? extends Composer> composerKey, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.composerKey = composerKey;
    }

    @Override
    public Key<? extends Composer> getComposerKey() {
        return composerKey;
    }

    @Override
    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new ComposerKeyBindingImpl<>(getKey(), scoping, composerKey);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(ComposerKeyBinding.class)
                .add("key", getKey())
                .add("composerKey", composerKey)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ComposerKeyBinding) {
            ComposerKeyBinding<?> other = (ComposerKeyBinding<?>) o;
            return getKey().equals(other.getKey())
                    && composerKey.equals(other.getComposerKey());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), composerKey);
    }
}
