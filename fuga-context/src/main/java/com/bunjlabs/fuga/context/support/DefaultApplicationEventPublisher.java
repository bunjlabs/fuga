package com.bunjlabs.fuga.context.support;

import com.bunjlabs.fuga.context.ApplicationEvent;
import com.bunjlabs.fuga.context.ApplicationEventDispatcher;
import com.bunjlabs.fuga.context.ApplicationEventPublisher;
import com.bunjlabs.fuga.context.events.PayloadApplicationEvent;
import com.bunjlabs.fuga.util.Assert;

public class DefaultApplicationEventPublisher implements ApplicationEventPublisher {
    private final ApplicationEventDispatcher eventDispatcher;

    public DefaultApplicationEventPublisher(ApplicationEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void publish(Object event) {
        Assert.notNull(event, "Event must not be null");

        ApplicationEvent applicationEvent;
        if (event instanceof ApplicationEvent) {
            applicationEvent = (ApplicationEvent) event;
        } else {
            applicationEvent = new PayloadApplicationEvent<>(this, event);
        }

        eventDispatcher.dispatch(applicationEvent);
    }
}
