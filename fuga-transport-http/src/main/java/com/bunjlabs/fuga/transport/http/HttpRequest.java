/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.transport.http;

import org.eclipse.jetty.server.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class HttpRequest {

    private final String target;
    private final Request baseRequest;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public HttpRequest(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        this.target = target;
        this.baseRequest = baseRequest;
        this.request = request;
        this.response = response;
    }

    public String getTarget() {
        return target;
    }

    public Request getBaseRequest() {
        return baseRequest;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

}
