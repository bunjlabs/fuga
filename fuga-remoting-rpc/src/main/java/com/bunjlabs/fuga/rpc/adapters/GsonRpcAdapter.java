package com.bunjlabs.fuga.rpc.adapters;

import com.bunjlabs.fuga.channel.ChannelAdapter;
import com.bunjlabs.fuga.channel.ChannelHandlerException;
import com.bunjlabs.fuga.rpc.RpcRequest;
import com.bunjlabs.fuga.transport.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class GsonRpcAdapter extends ChannelAdapter<HttpRequest, RpcRequest> {

    private final Gson gson = new Gson();
    private final Type mapType = new TypeToken<Map<String, String>>(){}.getType();

    @Override
    public void handle(HttpRequest request) throws ChannelHandlerException {
        String target = request.getTarget();
        Map<String, Object> parameters;

        try {
            parameters = gson.fromJson(request.getRequest().getReader(), mapType);
        } catch (IOException e) {
            throw new ChannelHandlerException(e);
        }

        RpcRequest rpcRequest = new RpcRequest(target, parameters);

        this.fireHandlers(rpcRequest);

        try {
            gson.toJson(rpcRequest.getResult(), request.getResponse().getWriter());
        } catch (IOException e) {
            throw new ChannelHandlerException(e);
        }

        request.getResponse().setContentType("application/json");
        request.getBaseRequest().setHandled(true);
    }
}
