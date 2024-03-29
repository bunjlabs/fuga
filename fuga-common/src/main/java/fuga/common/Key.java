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

package fuga.common;

import fuga.lang.TypeLiteral;

import java.lang.reflect.Type;
import java.util.Objects;

public class Key<T> {

    private final TypeLiteral<T> type;
    private final int hashCode;
    private String toString;

    public static Key<?> of(Type type) {
        return new Key<>(TypeLiteral.of(type));
    }

    public static <T> Key<T> of(Class<T> type) {
        return new Key<>(TypeLiteral.of(type));
    }

    public static <T> Key<T> of(TypeLiteral<T> type) {
        return new Key<>(type);
    }

    private Key(TypeLiteral<T> type) {
        this.type = type;
        this.hashCode = computeHashCode();
    }

    public Class<T> getRawType() {
        return type.getRawType();
    }

    public TypeLiteral<T> getType() {
        return type;
    }

    private int computeHashCode() {
        return Objects.hash(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key<?> key = (Key<?>) o;
        return type.equals(key.type);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        String s = toString;
        if (s == null) {
            s = Key.class.getSimpleName() + "[" + type + "]";
            toString = s;
        }
        return s;
    }
}
