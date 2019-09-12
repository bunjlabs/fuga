package com.bunjlabs.fuga.context.events;


import com.bunjlabs.fuga.context.ApplicationContext;

public class ContextClosedEvent extends ApplicationContextEvent {

    public ContextClosedEvent(ApplicationContext context) {
        super(context);
    }
}


