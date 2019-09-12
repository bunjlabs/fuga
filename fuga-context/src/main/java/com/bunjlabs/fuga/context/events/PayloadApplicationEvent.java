package com.bunjlabs.fuga.context.events;

import com.bunjlabs.fuga.context.ApplicationEvent;
import com.bunjlabs.fuga.util.Assert;

public class PayloadApplicationEvent<T> extends ApplicationEvent {

    private final T payload;

    public PayloadApplicationEvent(Object source, T payload) {
        super(source);
        this.payload = Assert.notNull(payload, "Payload must not be null");
    }

    public T getPayload() {
        return this.payload;
    }
}
