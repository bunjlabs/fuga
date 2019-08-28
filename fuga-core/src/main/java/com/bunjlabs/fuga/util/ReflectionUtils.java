package com.bunjlabs.fuga.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public abstract class ReflectionUtils {

    public static boolean isJdkDynamicProxy(Object object) {
        return Proxy.isProxyClass(object.getClass());
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<? super T>[] getProxiedInterfaces(T proxy) {
        var proxyInterfaces = proxy.getClass().getInterfaces();

        Assert.notEmpty(proxyInterfaces, "dynamic proxy must implement one or more interfaces");

        return (Class<? super T>[]) proxyInterfaces;
    }

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
}
