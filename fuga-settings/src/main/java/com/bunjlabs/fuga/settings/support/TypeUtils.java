package com.bunjlabs.fuga.settings.support;

import com.bunjlabs.fuga.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class TypeUtils {

    private static final Map<Class<?>, Function<String, ?>> primitiveParsers = new HashMap<>();

    static {
        primitiveParsers.put(String.class, s -> s);
        primitiveParsers.put(byte.class, Byte::parseByte);
        primitiveParsers.put(short.class, Short::parseShort);
        primitiveParsers.put(int.class, Integer::parseInt);
        primitiveParsers.put(long.class, Long::parseLong);
        primitiveParsers.put(float.class, Float::parseFloat);
        primitiveParsers.put(double.class, Double::parseDouble);
        primitiveParsers.put(boolean.class, Boolean::parseBoolean);
        primitiveParsers.put(char.class, s -> s.charAt(0));
    }

    public static Object convertStringToPrimitive(String input, Class<?> type) {
        Assert.notNull(input);
        Assert.notNull(type);

        Function<String, ?> converter = primitiveParsers.get(type);

        return converter.apply(input);
    }
}
