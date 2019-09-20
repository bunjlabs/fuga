package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;

import java.lang.reflect.InvocationTargetException;

public interface ConstructorProxy<T> {

    T newInstance(Object[] parameters) throws InvocationTargetException;

    Key<T> getType();

    Key<?>[] getParameterTypes();
}
