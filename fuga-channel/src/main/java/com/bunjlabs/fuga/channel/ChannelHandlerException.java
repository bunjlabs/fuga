/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.channel;

public class ChannelHandlerException extends Exception {

    public ChannelHandlerException() {
    }

    public ChannelHandlerException(String msg) {
        super(msg);
    }

    public ChannelHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelHandlerException(Throwable cause) {
        super(cause);
    }

}
