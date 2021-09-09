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

public abstract class Assert {

    public static <T> T notNull(T object) {
        return notNull(object, "[Assertion failed] - must not be null");
    }

    public static <T> T notNull(T object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
        return object;
    }

    public static boolean isFalse(boolean expression) {
        return isFalse(expression, "[Assertion failed] - must be false");
    }

    public static boolean isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
        return expression;
    }

    public static boolean isTrue(boolean expression) {
        return isTrue(expression, "[Assertion failed] - must be true");
    }

    public static boolean isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
        return expression;
    }

    public static String hasText(String str) {
        return hasText(str, "[Assertion failed] - string must not be empty");
    }

    public static String hasText(String str, String message) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return str;
    }

    public static <T> T[] notEmpty(T[] array) {
        return notEmpty(array, "[Assertion failed] - array must not be empty");
    }

    public static <T> T[] notEmpty(T[] array, String message) {
        if (array.length <= 0) {
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static <T> T[] isEmpty(T[] array) {
        return isEmpty(array, "[Assertion failed] - array must be empty");
    }

    public static <T> T[] isEmpty(T[] array, String message) {
        if (array.length > 0) {
            throw new IllegalArgumentException(message);
        }
        return array;
    }
}

