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

package fuga.util;

import java.util.StringJoiner;

public abstract class ObjectUtils {

    private static final String EMPTY_STRING = "";

    public static String identityToString(Object obj) {
        if (obj == null) {
            return EMPTY_STRING;
        }
        return obj.getClass().getName() + "@" + getIdentityHexString(obj);
    }

    public static String getIdentityHexString(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }

    public static ToStringJoiner toStringJoiner(Object obj) {
        Assert.notNull(obj);
        return new ToStringJoiner(obj.getClass().getSimpleName());
    }

    public static ToStringJoiner toStringJoiner(Class<?> clazz) {
        Assert.notNull(clazz);
        return new ToStringJoiner(clazz.getSimpleName());
    }

    public static ToStringJoiner toStringJoiner(String className) {
        Assert.hasText(className);
        return new ToStringJoiner(className);
    }

    public static final class ToStringJoiner {

        private final StringJoiner stringJoiner;

        public ToStringJoiner(String className) {
            this.stringJoiner = new StringJoiner(", ", className + "[", "]");
        }

        public ToStringJoiner add(String name, int value) {
            return add(name, String.valueOf(value));
        }

        public ToStringJoiner add(String name, long value) {
            return add(name, String.valueOf(value));
        }

        public ToStringJoiner add(String name, float value) {
            return add(name, String.valueOf(value));
        }

        public ToStringJoiner add(String name, double value) {
            return add(name, String.valueOf(value));
        }

        public ToStringJoiner add(String name, char value) {
            return add(name, String.valueOf(value));
        }

        public ToStringJoiner add(String name, boolean value) {
            return add(name, String.valueOf(value));
        }

        public ToStringJoiner add(String name, Object value) {
            stringJoiner.add(name + "=" + value);
            return this;
        }

        @Override
        public String toString() {
            return stringJoiner.toString();
        }
    }
}
