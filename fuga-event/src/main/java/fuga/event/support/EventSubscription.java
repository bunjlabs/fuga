package fuga.event.support;

import fuga.event.Subscriber;
import fuga.event.Subscription;

import java.util.concurrent.Executor;

abstract class EventSubscription<T> implements Subscription<T> {
    private final Subscriber<T> subscriber;
    private final Executor executor;
    private final SubscriberAgent<T> subscriberAgent;

    EventSubscription(Subscriber<T> subscriber, Executor executor, SubscriberAgent<T> subscriberAgent) {
        this.subscriber = subscriber;
        this.executor = executor;
        this.subscriberAgent = subscriberAgent;
    }

    @Override
    public Subscriber<T> getSubscriber() {
        return subscriber;
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public void unsubscribe() {
        subscriberAgent.clear();
        unsubscribe(subscriberAgent);
    }

    abstract void unsubscribe(SubscriberAgent<T> agent);
}
