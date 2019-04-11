package com.bunjlabs.fuga.channel;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ChannelAdapter<T1, T2> implements ChannelHandler<T1>, Channel<T2> {

    private final List<ChannelHandler<T2>> handlers = new CopyOnWriteArrayList<>();

    protected final void fireHandlers(T2 request) throws ChannelHandlerException {
        for (ChannelHandler<T2> h : handlers) {
            h.handle(request);
        }
    }

    @Override
    public final void registerChannelHandler(ChannelHandler<T2> channelHandler) {
        handlers.add(channelHandler);
    }
}
