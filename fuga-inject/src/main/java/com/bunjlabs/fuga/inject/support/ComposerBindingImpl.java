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

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.bindings.ComposerBinding;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

class ComposerBindingImpl<T> extends AbstractBinding<T> implements ComposerBinding<T> {

    private final Composer composer;

    ComposerBindingImpl(Key<T> key, Scoping scoping, Composer composer) {
        super(key, scoping);
        this.composer = composer;
    }

    ComposerBindingImpl(Key<T> key, Composer composer, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.composer = composer;
    }

    @Override
    public Composer getComposer() {
        return composer;
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new ComposerBindingImpl<>(getKey(), scoping, composer);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(ComposerBinding.class)
                .add("key", getKey())
                .add("composer", composer)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ComposerBinding) {
            ComposerBinding<?> other = (ComposerBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(composer, other.getComposer());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), composer);
    }
}
