package fuga.event.support;

import fuga.common.Key;
import fuga.event.EventBus;
import fuga.event.Subscriber;
import fuga.event.Subscription;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultEventBus implements EventBus {
    private final ConcurrentMap<Key<?>, EventAgent<?>> events;

    public DefaultEventBus() {
        this.events = new ConcurrentHashMap<>();
    }

    @Override
    public <T> Subscription subscribe(Class<T> event, Subscriber<T> subscriber) {
        return subscribe(Key.of(event), subscriber);
    }

    @Override
    public <T> Subscription subscribe(Key<T> eventType, Subscriber<T> subscriber) {
        var agent = getEvent(eventType);
        return agent.subscribe(subscriber);
    }

    @Override
    public void fire(Object event) {
        var eventKey = Key.of(event.getClass());

        @SuppressWarnings("unchecked")
        var agent = (EventAgent<Object>) getEvent(eventKey);

        agent.fire(event);
    }

    private <T> EventAgent<T> getEvent(Key<T> eventType) {
        @SuppressWarnings("unchecked")
        var agent = (EventAgent<T>) events.get(eventType);

        if (agent == null) {
            events.put(eventType, agent = new EventAgent<>());
        }
        return agent;
    }
}
