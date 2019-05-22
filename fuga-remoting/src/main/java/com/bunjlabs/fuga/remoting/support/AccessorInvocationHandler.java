package com.bunjlabs.fuga.remoting.support;

import com.bunjlabs.fuga.remoting.RemoteCall;
import com.bunjlabs.fuga.remoting.RemoteCallResult;
import com.bunjlabs.fuga.remoting.RemoteExecutor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AccessorInvocationHandler implements InvocationHandler {

    private final RemoteExecutor executor;

    AccessorInvocationHandler(RemoteExecutor executor) {
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
