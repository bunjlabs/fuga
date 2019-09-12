package com.bunjlabs.fuga.util;

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

    public static String hasText(String str, String message) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return str;
    }

    public static <T> T[] notEmpty(T[] array, String message) {
        if (array.length <= 0) {
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static <T> T[] isEmpty(T[] array, String message) {
        if (array.length > 0) {
            throw new IllegalArgumentException(message);
        }
        return array;
    }
}

