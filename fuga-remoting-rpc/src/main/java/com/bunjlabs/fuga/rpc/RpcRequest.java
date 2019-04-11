/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.rpc;

import java.util.Map;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class RpcRequest {

    private final String target;
    private final Map<String, Object> parameters;

    private Object result;

    public RpcRequest(String target, Map<String, Object> parameters) {
        this.target = target;
        this.parameters = parameters;
    }

    public String getTarget() {
        return target;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
