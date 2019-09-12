package com.bunjlabs.fuga.context;

public interface ApplicationEventPublisher {

    default void publish(ApplicationEvent event) {
        publish((Object) event);
    }

    void publish(Object event);
}
