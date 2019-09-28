package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.bindings.InstanceBinding;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

class InstanceBindingImpl<T> extends AbstractBinding<T> implements InstanceBinding<T> {

    private final T instance;

    InstanceBindingImpl(Key<T> key, Scoping scoping, T instance) {
        super(key, scoping);
        this.instance = instance;
    }

    InstanceBindingImpl(Key<T> key, T instance, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.instance = instance;
    }

    @Override
    public T getInstance() {
        return instance;
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new InstanceBindingImpl<>(getKey(), scoping, instance);
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
                    && Objects.equals(instance, other.getInstance());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), instance);
    }

}
