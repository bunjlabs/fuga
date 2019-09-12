package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

public class ConstructorBinding<T> extends AbstractBinding<T> {
    private final InjectionPoint injectionPoint;

    ConstructorBinding(Key<T> key, InjectionPoint injectionPoint) {
        super(key);
        this.injectionPoint = injectionPoint;
    }

    ConstructorBinding(Key<T> key, InjectionPoint injectionPoint, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.injectionPoint = injectionPoint;
    }

    InjectionPoint getInjectionPoint() {
        return injectionPoint;
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
                    && Objects.equals(injectionPoint, other.injectionPoint);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), injectionPoint);
    }
    
    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }
}
