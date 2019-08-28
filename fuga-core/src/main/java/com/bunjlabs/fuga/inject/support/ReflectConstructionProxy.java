package com.bunjlabs.fuga.inject.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectConstructionProxy<T> implements ConstructorProxy<T> {
    private final Constructor<T> constructor;

    ReflectConstructionProxy(Constructor<T> constructor) {
        this.constructor = constructor;
    }

    @Override
    public T newInstance(Object[] parameters) throws InvocationTargetException {
        try {
            return constructor.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }
}
