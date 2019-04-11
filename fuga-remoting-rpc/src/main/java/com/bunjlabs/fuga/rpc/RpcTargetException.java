/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.rpc;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class RpcTargetException extends Exception {

    public RpcTargetException() {
    }

    public RpcTargetException(String message) {
        super(message);
    }

    public RpcTargetException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcTargetException(Throwable cause) {
        super(cause);
    }

}
