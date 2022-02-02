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
import fuga.inject.BindingVisitor;
import fuga.inject.bindings.UntargetedBinding;
import fuga.util.ObjectUtils;

import java.util.Objects;

class UntargetedBindingImpl<T> extends AbstractBinding<T> implements UntargetedBinding<T> {

    UntargetedBindingImpl(Key<T> key) {
        super(key);
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(UntargetedBinding.class)
                .add("key", getKey())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UntargetedBinding) {
            UntargetedBinding<?> other = (UntargetedBinding<?>) o;
            return getKey().equals(other.getKey());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

}
