package com.bunjlabs.fuga.remoting;

import com.bunjlabs.fuga.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RemoteExporter {

    private Object service;

    private Class<?> serviceInterface;

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        Assert.notNull(serviceInterface, "'service' must not be null");

        this.service = service;
    }

    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        Assert.notNull(serviceInterface, "'serviceInterface' must not be null");
        Assert.isTrue(serviceInterface.isInterface(), "'serviceInterface' must be an interface");

        this.serviceInterface = serviceInterface;
    }

    public RemoteCallResult handleCall(RemoteCall call) throws NoSuchMethodException, IllegalAccessException {
        Method callMethod = serviceInterface.getMethod(call.getMethodName(), call.getParameterTypes());

        RemoteCallResult callResult = new RemoteCallResult();

        try {
            Object value = callMethod.invoke(service, call.getArguments());

            callResult.setValue(value);
        } catch (InvocationTargetException e) {
            callResult.setException(e.getTargetException());
        }

        return callResult;
    }
}
