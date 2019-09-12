package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Key;

public abstract class AbstractBinding<T> implements Binding<T> {

    private final Key<T> key;
    private final InternalFactory<T> internalFactory;

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

    InternalFactory<T> getInternalFactory() {
        return internalFactory;
    }
}
