package fuga.event.support;

import fuga.event.Subscriber;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;

class SubscriberAgent<T> extends WeakReference<Subscriber<T>>{

    private final Executor executor;

    SubscriberAgent(Subscriber<T> subscriber, Executor executor, ReferenceQueue<Subscriber<T>> queue) {
        super(subscriber, queue);
        this.executor = executor;
    }

    void fire(T event) {
        var subscriber = get();
        if (subscriber != null) {
            executor.execute(() -> invokeSubscriber(subscriber, event));
        }
    }

    private void invokeSubscriber(Subscriber<T> subscriber, T event) {
        subscriber.onEvent(event);
    }
}
