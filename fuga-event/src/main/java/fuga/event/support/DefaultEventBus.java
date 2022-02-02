package fuga.event.support;

import fuga.common.Key;
import fuga.event.DeadEvent;
import fuga.event.EventBus;
import fuga.event.Subscriber;
import fuga.event.Subscription;
import fuga.util.concurrent.CurrentThreadExecutor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

public class DefaultEventBus implements EventBus {
    private final ConcurrentMap<Key<?>, EventAgent<?>> events = new ConcurrentHashMap<>();
    private final Executor defaultExecutor;

    public DefaultEventBus() {
        this.defaultExecutor = CurrentThreadExecutor.INSTANCE;
    }

    public DefaultEventBus(Executor executor) {
        this.defaultExecutor = executor;
    }

    @Override
    public <T> Subscription<T> subscribe(Class<T> event, Subscriber<T> subscriber) {
        return subscribe(Key.of(event), subscriber, defaultExecutor);
    }

    @Override
    public <T> Subscription<T> subscribe(Key<T> eventType, Subscriber<T> subscriber) {
        return subscribe(eventType, subscriber, defaultExecutor);
    }

    @Override
    public <T> Subscription<T> subscribe(Class<T> event, Subscriber<T> subscriber, Executor executor) {
        return subscribe(Key.of(event), subscriber, executor);
    }

    @Override
    public <T> Subscription<T> subscribe(Key<T> eventType, Subscriber<T> subscriber, Executor executor) {
        var agent = getEvent(eventType);
        return agent.subscribe(subscriber, executor);
    }

    @Override
    public void post(Object event) {
        var eventKey = Key.of(event.getClass());

        @SuppressWarnings("unchecked")
        var agent = (EventAgent<Object>) getEvent(eventKey);

        if (agent.ensureSubscribers()) {
            agent.fire(event);
        } else if (!(event instanceof DeadEvent)) {
            post(new DeadEvent(this, event));
        }
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
