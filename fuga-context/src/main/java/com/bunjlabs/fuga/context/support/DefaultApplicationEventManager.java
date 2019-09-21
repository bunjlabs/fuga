package com.bunjlabs.fuga.context.support;

import com.bunjlabs.fuga.context.ApplicationEvent;
import com.bunjlabs.fuga.context.ApplicationEventDispatcher;
import com.bunjlabs.fuga.context.ApplicationEventManager;
import com.bunjlabs.fuga.context.ApplicationListener;
import com.bunjlabs.fuga.util.FullType;

public class DefaultApplicationEventManager implements ApplicationEventManager {

    private final ApplicationEventDispatcher eventDispatcher;

    public DefaultApplicationEventManager(ApplicationEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public <T extends ApplicationEvent> void addEventListener(ApplicationListener<T> listener) {
        var genericType = FullType.of(listener.getClass()).as(ApplicationListener.class).getGeneric(0);
        if (genericType == FullType.EMPTY || !ApplicationEvent.class.isAssignableFrom(genericType.getRawType())) {
            throw new IllegalArgumentException();
        }


        @SuppressWarnings("unchecked")
        FullType<? extends ApplicationEvent> eventType = genericType;

        eventDispatcher.addEventListener(genericType.getRawType(), listener);
    }
}
