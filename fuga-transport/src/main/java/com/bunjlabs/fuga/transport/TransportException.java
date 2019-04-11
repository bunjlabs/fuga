/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.transport;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TransportException extends Exception {

    public TransportException() {
    }

    public TransportException(String msg) {
        super(msg);
    }

    public TransportException(Throwable cause) {
        super(cause);
    }

    public TransportException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
