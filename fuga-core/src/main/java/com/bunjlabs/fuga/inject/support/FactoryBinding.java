package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Factory;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

public class FactoryBinding<T> extends AbstractBinding<T> {
    private final Factory factory;

    FactoryBinding(Key<T> key, Factory factory) {
        super(key);
        this.factory = factory;
    }

    Factory getFactory() {
        return factory;
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("key", getKey())
                .add("factory", factory)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FactoryBinding) {
            FactoryBinding<?> other = (FactoryBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(factory, other.factory);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), factory);
    }

    @Override
    public InternalFactory<T> getInternalFactory() {
        return new ProxyFactory<>(factory);
    }
}
