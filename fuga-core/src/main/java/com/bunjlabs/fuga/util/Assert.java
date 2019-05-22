package com.bunjlabs.fuga.util;

public abstract class Assert {

    public static <T> T notNull(T object) {
        return notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static <T> T notNull(T object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }

        return object;
    }

    public static boolean isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
        return expression;
    }

    public static boolean isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
        return expression;
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

