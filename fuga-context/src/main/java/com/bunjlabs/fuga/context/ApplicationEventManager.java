package com.bunjlabs.fuga.context;

public interface ApplicationEventManager {

    <T extends ApplicationEvent> void addEventListener(ApplicationListener<T> listener);
}
