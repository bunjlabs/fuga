package com.bunjlabs.fuga.context.events;

import com.bunjlabs.fuga.context.ApplicationContext;

public class ContextStartedEvent extends ApplicationContextEvent {
    public ContextStartedEvent(ApplicationContext context) {
        super(context);
    }
}
