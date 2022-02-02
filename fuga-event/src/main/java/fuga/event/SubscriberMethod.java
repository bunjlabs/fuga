package fuga.event;

import fuga.common.Key;

import java.lang.reflect.Method;

public class SubscriberMethod {
    private final Method method;
    private final Key<?> eventType;
    private final Subscribe annotation;

    public SubscriberMethod(Method method, Key<?> eventType, Subscribe annotation) {
        this.method = method;
        this.eventType = eventType;
        this.annotation = annotation;
    }

    public Method getMethod() {
        return method;
    }

    public Key<?> getEventType() {
        return eventType;
    }

    public Subscribe getAnnotation() {
        return annotation;
    }
}
