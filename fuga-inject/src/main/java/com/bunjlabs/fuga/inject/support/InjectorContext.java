package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;

import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

class InjectorContext {

    private final InjectorImpl injector;
    private final Deque<Key<?>> requesters;
    private final Map<Object, ConstructionContext<?>> constructionContexts = new IdentityHashMap<>();

    InjectorContext(InjectorImpl injector) {
        this.injector = injector;
        this.requesters = new ConcurrentLinkedDeque<>();
    }

    Key<?> getRequester() {
        return requesters.peekFirst();
    }

    void enterRequester(Key<?> requester) {
        requesters.addFirst(requester);
    }

    void exitRequester() {
        requesters.removeFirst();
    }

    InjectorImpl getInjector() {
        return injector;
    }

    <T> ConstructionContext<T> getConstructionContext(Object key) {
        @SuppressWarnings("unchecked")
        ConstructionContext<T> constructionContext =
                (ConstructionContext<T>) constructionContexts.get(key);

        if (constructionContext == null) {
            constructionContext = new ConstructionContext<>();
            constructionContexts.put(key, constructionContext);
        }

        return constructionContext;
    }
}
