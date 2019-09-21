package com.bunjlabs.fuga.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public abstract class ReflectionUtils {

    private static final List<Method> OBJECT_METHODS = Arrays.asList(Object.class.getMethods());


    public static List<Class<?>> getParameterTypes(Member methodOrConstructor) {
        Class<?>[] parameterTypes;

        if (methodOrConstructor instanceof Constructor) {
            Constructor<?> constructor = (Constructor<?>) methodOrConstructor;
            parameterTypes = constructor.getParameterTypes();
        } else if (methodOrConstructor instanceof Method) {
            Method method = (Method) methodOrConstructor;
            parameterTypes = method.getParameterTypes();
        } else {
            throw new IllegalArgumentException("Not a method or a constructor: " + methodOrConstructor);
        }

        return List.of(parameterTypes);
    }

    public static boolean allowsNull(Annotation[] annotations) {
        for (Annotation a : annotations) {
            Class<? extends Annotation> type = a.annotationType();
            if ("Nullable".equals(type.getSimpleName())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isObjectMethod(Method m) {
        return OBJECT_METHODS.contains(m);
    }
}
