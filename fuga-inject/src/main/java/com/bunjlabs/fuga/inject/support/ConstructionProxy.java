package com.bunjlabs.fuga.inject.support;

import java.lang.reflect.InvocationTargetException;

interface ConstructionProxy<T> {

    T newInstance(Object... parameters) throws InvocationTargetException;
}
