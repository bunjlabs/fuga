package com.bunjlabs.fuga.inject.support;

import java.lang.reflect.InvocationTargetException;

public interface ConstructorProxy<T> {

    T newInstance(Object[] parameters) throws InvocationTargetException;

    Class<T> getType();

    Class<?>[] getParameterTypes();
}
