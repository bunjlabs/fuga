package fuga.event.support;

import fuga.event.Subscriber;
import fuga.event.Subscription;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class EventAgent<T> {

    private final Set<Subscriber<T>> subscribers;

    EventAgent() {
        this.subscribers = new CopyOnWriteArraySet<>();
    }

    Subscription subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
        return () -> subscribers.remove(subscriber);
    }

    void fire(T event) {
        subscribers.forEach(s -> s.onEvent(event));
    }
}
