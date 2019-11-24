package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Key;

abstract class AbstractBinding<T> implements Binding<T> {

    private final Key<T> key;
    private final InternalFactory<T> internalFactory;
    private final Scoping scoping;

    AbstractBinding(Key<T> key, Scoping scoping) {
        this.key = key;
        this.scoping = scoping;
        this.internalFactory = null;
    }

    AbstractBinding(Key<T> key, InternalFactory<T> internalFactory) {
        this.key = key;
        this.scoping = null;
        this.internalFactory = internalFactory;
    }

    @Override
    public Key<T> getKey() {
        return key;
    }

    public Scoping getScoping() {
        return scoping;
    }

    protected abstract AbstractBinding<T> withScoping(Scoping scoping);

    InternalFactory<T> getInternalFactory() {
        return internalFactory;
    }
}
