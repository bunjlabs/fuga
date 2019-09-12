package com.bunjlabs.fuga.context.support;

import com.bunjlabs.fuga.context.ApplicationEvent;
import com.bunjlabs.fuga.context.ApplicationEventDispatcher;
import com.bunjlabs.fuga.context.ApplicationEventManager;
import com.bunjlabs.fuga.context.ApplicationListener;

public class DefaultApplicationEventManager implements ApplicationEventManager {

    private final ApplicationEventDispatcher eventDispatcher;

    public DefaultApplicationEventManager(ApplicationEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public <T extends ApplicationEvent> void addEventListener(ApplicationListener<T> listener) {

        //eventDispatcher.addEventListener();
    }
}
