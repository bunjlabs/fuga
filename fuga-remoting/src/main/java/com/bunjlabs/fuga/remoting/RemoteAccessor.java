package com.bunjlabs.fuga.remoting;

import com.bunjlabs.fuga.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class RemoteAccessor {

    public <T> T getServiceProxy(RemoteExecutor executor, Class<T> serviceInterface) {
        Assert.notNull(serviceInterface, "'serviceInterface' must not be null");
        Assert.isTrue(serviceInterface.isInterface(), "'serviceInterface' must be an interface");

        InvocationHandler handler = new DefaultInvocationHandler(executor);

        return createProxy(serviceInterface, handler);
    }

    @SuppressWarnings("unchecked")
    private <T> T createProxy(Class<T> serviceInterface, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class[]{serviceInterface}, handler);
    }

    private static class DefaultInvocationHandler implements InvocationHandler {

        private final RemoteExecutor executor;

        DefaultInvocationHandler(RemoteExecutor executor) {
            this.executor = executor;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            RemoteCall call = new RemoteCall(method.getName(), method.getParameterTypes(), args);

            RemoteCallResult callResult = executor.invoke(call);

            if (callResult.hasException()) {
                throw callResult.getException();
            } else {
                return callResult.getValue();
            }
        }
    }
}
