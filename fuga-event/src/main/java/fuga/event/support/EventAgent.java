package fuga.event.support;

import fuga.event.Subscriber;
import fuga.event.Subscription;

import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

class EventAgent<T> {

    private final ReferenceQueue<Subscriber<T>> queue = new ReferenceQueue<>();
    private final Set<SubscriberAgent<T>> subscribers;

    EventAgent() {
        this.subscribers = new CopyOnWriteArraySet<>();
    }

    Subscription<T> subscribe(Subscriber<T> subscriber, Executor executor) {
        var subscriberAgent = new SubscriberAgent<>(subscriber, executor, queue);
        subscribers.add(subscriberAgent);

        return new EventSubscription<>(subscriber, executor, subscriberAgent) {
            @Override
            void unsubscribe(SubscriberAgent<T> agent) {
                subscribers.remove(agent);
            }
        };
    }

    boolean ensureSubscribers() {
        expungeSubscribers();

        return !subscribers.isEmpty();
    }

    void fire(T event) {
        subscribers.forEach(s -> s.fire(event));
    }

    private void expungeSubscribers() {
        for (Object x; (x = queue.poll()) != null; ) {
            synchronized (queue) {
                @SuppressWarnings("unchecked")
                var agent = (SubscriberAgent<T>) x;
                subscribers.remove(agent);
            }
        }
    }
}
