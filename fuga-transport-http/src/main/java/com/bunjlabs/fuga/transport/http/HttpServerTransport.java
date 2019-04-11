/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.transport.http;

import com.bunjlabs.fuga.channel.ChannelHandler;
import com.bunjlabs.fuga.dependency.annotations.Inject;
import com.bunjlabs.fuga.transport.Transport;
import com.bunjlabs.fuga.transport.TransportException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ErrorHandler;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class HttpServerTransport implements Transport<HttpRequest> {

    private final Server server;
    private final HttpServerHandler handler;

    @Inject
    public HttpServerTransport() {
        this.server = new Server(8080);
        this.handler = new HttpServerHandler();
    }

    @Override
    public void enable() throws TransportException {
        this.server.setHandler(this.handler);
        this.server.setErrorHandler(new ErrorHandler());

        try {
            this.server.start();
        } catch (Exception ex) {
            throw new TransportException(ex);
        }
    }

    @Override
    public void registerChannelHandler(ChannelHandler<HttpRequest> channelHandler) {
        this.handler.registerChannelHandler(channelHandler);
    }

}
