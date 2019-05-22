package com.bunjlabs.fuga.remoting.support;

import com.bunjlabs.fuga.remoting.RemoteExecutor;
import com.bunjlabs.fuga.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;


public class DefaultRemoteAccessor implements com.bunjlabs.fuga.remoting.RemoteAccessor {

    @Override
    public <T> T getServiceProxy(RemoteExecutor executor, Class<T> serviceInterface) {
        Assert.notNull(serviceInterface, "'serviceInterface' must not be null");
        Assert.isTrue(serviceInterface.isInterface(), "'serviceInterface' must be an interface");

        InvocationHandler handler = new AccessorInvocationHandler(executor);

        return createProxy(serviceInterface, handler);
    }

    @SuppressWarnings("unchecked")
    private <T> T createProxy(Class<T> serviceInterface, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class[]{serviceInterface}, handler);
    }

}
