package com.bunjlabs.fuga.context;

import java.util.EventObject;

public abstract class ApplicationEvent extends EventObject {

    private static final long serialVersionUID = -8970983978500570925L;

    private final long timestamp;

    public ApplicationEvent(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
