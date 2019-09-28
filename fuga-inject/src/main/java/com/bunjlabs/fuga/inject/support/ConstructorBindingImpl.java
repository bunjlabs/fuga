package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.InjectionPoint;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.bindings.ConstructorBinding;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

class ConstructorBindingImpl<T> extends AbstractBinding<T> implements ConstructorBinding<T> {

    private final InjectionPoint injectionPoint;

    ConstructorBindingImpl(Key<T> key, Scoping scoping, InjectionPoint injectionPoint) {
        super(key, scoping);
        this.injectionPoint = injectionPoint;
    }

    ConstructorBindingImpl(Key<T> key, InjectionPoint injectionPoint, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.injectionPoint = injectionPoint;
    }

    @Override
    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new ConstructorBindingImpl<>(getKey(), scoping, injectionPoint);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("key", getKey())
                .add("injectionPoint", injectionPoint)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ConstructorBinding) {
            ConstructorBinding<?> other = (ConstructorBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(injectionPoint, other.getInjectionPoint());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), injectionPoint);
    }
}
