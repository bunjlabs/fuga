package com.bunjlabs.fuga.context.events;

import com.bunjlabs.fuga.context.ApplicationContext;
import com.bunjlabs.fuga.context.ApplicationEvent;

public abstract class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(ApplicationContext context) {
        super(context);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}
