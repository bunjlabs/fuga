package com.bunjlabs.fuga.context;

public interface ApplicationEventDispatcher {

    void dispatch(ApplicationEvent event);

    void addEventListener(Class<? extends ApplicationEvent> eventType, ApplicationListener listener);
}
