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

package fuga.inject;

import fuga.common.Key;

public interface Binding<T> {
    default <A> A getAttribute(Class<A> attributeType) {
        return getAttribute(Key.of(attributeType));
    }

    <A> A getAttribute(Key<A> attributeType);

    default <A> void setAttribute(Class<A> attributeType, A value) {
        setAttribute(Key.of(attributeType), value);
    }

    <A> void setAttribute(Key<A> attributeType, A value);

    Key<T> getKey();

    default Injector getInjector() {
        return getAttribute(Injector.class);
    }

    <V> V acceptVisitor(BindingVisitor<? super T, V> visitor);

}
