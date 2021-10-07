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

import fuga.inject.BindingVisitor;
import fuga.common.Key;
import fuga.inject.bindings.LinkedKeyBinding;
import fuga.util.ObjectUtils;

import java.util.Objects;

class LinkedKeyBindingImpl<T> extends AbstractBinding<T> implements LinkedKeyBinding<T> {

    private final Key<? extends T> linkedKey;

    LinkedKeyBindingImpl(Key<T> key, Scoping scoping, Key<? extends T> linkedKey) {
        super(key, scoping);
        this.linkedKey = linkedKey;
    }

    LinkedKeyBindingImpl(Key<T> key, Key<? extends T> linkedKey, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.linkedKey = linkedKey;
    }

    @Override
    public Key<? extends T> getLinkedKey() {
        return linkedKey;
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new LinkedKeyBindingImpl<>(getKey(), scoping, linkedKey);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(LinkedKeyBinding.class)
                .add("key", getKey())
                .add("linkedKey", linkedKey)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LinkedKeyBinding) {
            LinkedKeyBinding<?> other = (LinkedKeyBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(linkedKey, other.getLinkedKey());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), linkedKey);
    }
}
