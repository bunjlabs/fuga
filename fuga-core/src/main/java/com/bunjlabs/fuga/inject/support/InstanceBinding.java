package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

public class InstanceBinding<T> extends AbstractBinding<T> {

    private final T instance;

    InstanceBinding(Key<T> key, T instance) {
        super(key);
        this.instance = instance;
    }

    public T getInstance() {
        return instance;
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("key", getKey())
                .add("instance", instance)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof InstanceBinding) {
            InstanceBinding<?> other = (InstanceBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(instance, other.instance);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), instance);
    }

    @Override
    public InternalFactory<T> getInternalFactory() {
        return new InitializableFactory<>(() -> instance);
    }
}
