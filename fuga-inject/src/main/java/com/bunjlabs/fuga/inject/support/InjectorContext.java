package com.bunjlabs.fuga.inject.support;

import java.util.IdentityHashMap;
import java.util.Map;

class InjectorContext {

    private final DefaultInjector injector;
    private final Class<?> requester;
    private final Map<Object, ConstructionContext<?>> constructionContexts = new IdentityHashMap<>();

    InjectorContext(DefaultInjector injector, Class<?> requester) {
        this.injector = injector;
        this.requester = requester;
    }

    Class<?> getRequester() {
        return requester;
    }

    DefaultInjector getInjector() {
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
