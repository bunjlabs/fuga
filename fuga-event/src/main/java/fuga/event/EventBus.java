package fuga.event;

import fuga.common.Key;

public interface EventBus {

    <T> Subscription subscribe(Class<T> event, Subscriber<T> subscriber);

    <T> Subscription subscribe(Key<T> event, Subscriber<T> subscriber);

    void fire(Object event);
}
