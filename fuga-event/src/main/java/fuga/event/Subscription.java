package fuga.event;

import java.util.concurrent.Executor;

public interface Subscription<T> {

    void unsubscribe();

    Subscriber<T> getSubscriber();

    Executor getExecutor();
}
