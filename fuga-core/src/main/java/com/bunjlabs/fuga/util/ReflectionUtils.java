package com.bunjlabs.fuga.util;

import java.lang.reflect.Proxy;

public abstract class ReflectionUtils {

    public static boolean isJdkDynamicProxy(Object object) {
        return Proxy.isProxyClass(object.getClass());
    }

    public static Class<?>[] getProxiedInterfaces(Object proxy) {
        Class<?>[] proxyInterfaces = proxy.getClass().getInterfaces();

        Assert.notEmpty(proxyInterfaces, "dynamic proxy must implement one or more interfaces");

        return proxyInterfaces;
    }
}
