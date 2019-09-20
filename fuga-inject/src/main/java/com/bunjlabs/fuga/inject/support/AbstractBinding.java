package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Scope;

public abstract class AbstractBinding<T> implements Binding<T> {

    private final Key<T> key;
    private final InternalFactory<T> internalFactory;
    private Scope scope;

    AbstractBinding(Key<T> key) {
        this.key = key;
        this.internalFactory = null;
    }

    AbstractBinding(Key<T> key, InternalFactory<T> internalFactory) {
        this.key = key;
        this.internalFactory = internalFactory;
    }

    @Override
    public Key<T> getKey() {
        return key;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    InternalFactory<T> getInternalFactory() {
        return internalFactory;
    }
}
