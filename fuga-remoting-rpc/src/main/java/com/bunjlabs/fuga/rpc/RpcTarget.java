/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.rpc;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class RpcTarget {

    private final String target;
    private final Object service;
    private final Method method;

    public RpcTarget(String target, Object service, Method method) {
        this.target = target;
        this.service = service;
        this.method = method;
    }

    public Object invoke() throws RpcTargetException {
        try {
            return this.method.invoke(this.service);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RpcTargetException("Unable to execute RPC target", ex);
        }
    }

    public String getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public Object getService() {
        return service;
    }

}
