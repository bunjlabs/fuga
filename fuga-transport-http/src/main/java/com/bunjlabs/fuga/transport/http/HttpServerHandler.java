/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.transport.http;

import com.bunjlabs.fuga.channel.ChannelHandler;
import com.bunjlabs.fuga.channel.ChannelHandlerException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class HttpServerHandler extends AbstractHandler {

    private final List<ChannelHandler<HttpRequest>> nextHandlers = new CopyOnWriteArrayList<>();

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException, ServletException {
        HttpRequest request = new HttpRequest(target, baseRequest, servletRequest, servletResponse);

        for (ChannelHandler handler : nextHandlers) {
            try {
                handler.handle(request);
            } catch (ChannelHandlerException ex) {
                throw new ServletException(ex);
            }
        }
    }

    public void registerChannelHandler(ChannelHandler<HttpRequest> handler) {
        nextHandlers.add(handler);
    }
}
