package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Dependency;
import com.bunjlabs.fuga.inject.Injector;

import java.util.*;

class InjectorContext {

    private final InjectorImpl injector;
    private final List<Dependency<?>> dependencyStack;
    private final Map<Object, ConstructionContext<?>> constructionContexts = new IdentityHashMap<>();

    InjectorContext(InjectorImpl injector) {
        this.injector = injector;
        this.dependencyStack = Collections.synchronizedList(new ArrayList<>());
        pushDependency(Dependency.of(Injector.class));
    }

    InjectorImpl getInjector() {
        return injector;
    }

    Dependency<?> getDependency() {
        var size = dependencyStack.size();
        return size > 0 ? dependencyStack.get(size - 1) : null;
    }

    Dependency<?> getRequester() {
        var size = dependencyStack.size();
        return size > 1 ? dependencyStack.get(size - 2) : null;
    }

    void pushDependency(Dependency<?> dependency) {
        dependencyStack.add(dependency);
    }

    void popDependency() {
        var size = dependencyStack.size();
        if (size > 0) {
            dependencyStack.remove(size - 1);
        }
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
