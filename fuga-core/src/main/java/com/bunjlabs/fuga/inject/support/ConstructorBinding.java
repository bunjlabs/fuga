package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class ConstructorBinding<T> extends AbstractBinding<T> {
    private final InjectionPoint injectionPoint;

    ConstructorBinding(Key<T> key, InjectionPoint injectionPoint) {
        super(key);
        this.injectionPoint = injectionPoint;
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

    @Override
    public InternalFactory<T> getInternalFactory() {
        @SuppressWarnings("unchecked")
        Constructor<T> constructor = (Constructor<T>) injectionPoint.getMember();
        return new ConstructorFactory<>(new ReflectConstructionProxy<>(constructor));
    }

}
