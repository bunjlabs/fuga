package com.bunjlabs.fuga.remoting.support;

import com.bunjlabs.fuga.remoting.RemoteCall;
import com.bunjlabs.fuga.remoting.RemoteCallResult;
import com.bunjlabs.fuga.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DefaultRemoteExporter implements com.bunjlabs.fuga.remoting.RemoteExporter {

    private final Object service;
    private final Class<?> serviceInterface;

    public DefaultRemoteExporter(Object service, Class<?> serviceInterface) {
        Assert.notNull(serviceInterface, "'service' must not be null");
        Assert.notNull(serviceInterface, "'serviceInterface' must not be null");
        Assert.isTrue(serviceInterface.isInterface(), "'serviceInterface' must be an interface");

        this.service = service;
        this.serviceInterface = serviceInterface;
    }

    @Override
    public RemoteCallResult handleCall(RemoteCall call) throws NoSuchMethodException, IllegalAccessException {
        Method callMethod = serviceInterface.getMethod(call.getMethodName(), call.getParameterTypes());

        RemoteCallResult callResult;

        try {
            Object value = callMethod.invoke(service, call.getArguments());

            callResult = new DefaultRemoteCallResult(value);
        } catch (InvocationTargetException e) {
            callResult = new DefaultRemoteCallResult(e.getTargetException());
        }

        return callResult;
    }
}
