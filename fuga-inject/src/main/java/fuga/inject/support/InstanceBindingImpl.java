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
import fuga.inject.Key;
import fuga.inject.bindings.InstanceBinding;
import fuga.util.ObjectUtils;

import java.util.Objects;

class InstanceBindingImpl<T> extends AbstractBinding<T> implements InstanceBinding<T> {

    private final T instance;

    InstanceBindingImpl(Key<T> key, Scoping scoping, T instance) {
        super(key, scoping);
        this.instance = instance;
    }

    InstanceBindingImpl(Key<T> key, T instance, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.instance = instance;
    }

    @Override
    public T getInstance() {
        return instance;
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new InstanceBindingImpl<>(getKey(), scoping, instance);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(InstanceBinding.class)
                .add("key", getKey())
                .add("instance", instance)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof InstanceBinding) {
            InstanceBinding<?> other = (InstanceBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(instance, other.getInstance());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), instance);
    }

}
