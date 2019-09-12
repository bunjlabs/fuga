package com.bunjlabs.fuga.inject.support;

import java.util.IdentityHashMap;
import java.util.Map;

class InjectorContext {

    private final DefaultInjector injector;
    private final Map<Object, ConstructionContext<?>> constructionContexts = new IdentityHashMap<>();

    InjectorContext(DefaultInjector injector) {
        this.injector = injector;
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
