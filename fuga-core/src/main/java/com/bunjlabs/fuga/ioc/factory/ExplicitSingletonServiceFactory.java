package com.bunjlabs.fuga.ioc.factory;

import com.bunjlabs.fuga.ioc.DependencyException;

public class ExplicitSingletonServiceFactory<T> implements ServiceFactory<T> {

    private final T instance;

    public ExplicitSingletonServiceFactory(T instance) {
        this.instance = instance;
    }

    @Override
    public T getService(Object... arguments) throws DependencyException {
        return instance;
    }

    @Override
    public Class[] getDependencyTypes() {
        return new Class[0];
    }
}
