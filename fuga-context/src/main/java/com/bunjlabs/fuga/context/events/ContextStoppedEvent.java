package com.bunjlabs.fuga.context.events;


import com.bunjlabs.fuga.context.ApplicationContext;

public class ContextStoppedEvent extends ApplicationContextEvent {

    public ContextStoppedEvent(ApplicationContext context) {
        super(context);
    }
}


