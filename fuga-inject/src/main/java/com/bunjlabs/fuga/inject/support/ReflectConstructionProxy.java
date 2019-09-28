package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class ReflectConstructionProxy<T> implements ConstructorProxy<T> {

    private final Constructor<T> constructor;
    private final Key<T> type;
    private final Key<?>[] parameterTypes;

    ReflectConstructionProxy(Constructor<T> constructor) {
        this.constructor = constructor;
        this.type = Key.of(constructor.getDeclaringClass());

        Class<?>[] rawParameterTypes = constructor.getParameterTypes();
        this.parameterTypes = new Key[rawParameterTypes.length];

        for (int i = 0; i < this.parameterTypes.length; i++) {
            this.parameterTypes[i] = Key.of(rawParameterTypes[i]);
        }
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
    public Key<T> getType() {
        return type;
    }

    @Override
    public Key<?>[] getParameterTypes() {
        return parameterTypes;
    }
}
