package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Key;

public abstract class AbstractBinding<T> implements Binding<T> {

    private final Key<T> key;

    public AbstractBinding(Key<T> key) {
        this.key = key;
    }

    public abstract InternalFactory<T> getInternalFactory();

    @Override
    public Key<T> getKey() {
        return key;
    }
}
