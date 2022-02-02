package fuga.event;

import fuga.common.Key;

import java.util.concurrent.Executor;

public interface EventBus {

    <T> Subscription<T> subscribe(Class<T> event, Subscriber<T> subscriber);

    <T> Subscription<T> subscribe(Key<T> event, Subscriber<T> subscriber);

    <T> Subscription<T> subscribe(Class<T> event, Subscriber<T> subscriber, Executor executor);

    <T> Subscription<T> subscribe(Key<T> eventType, Subscriber<T> subscriber, Executor executor);

    void post(Object event);
}
