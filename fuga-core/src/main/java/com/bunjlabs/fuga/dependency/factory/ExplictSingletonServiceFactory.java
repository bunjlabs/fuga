package com.bunjlabs.fuga.dependency.factory;

import com.bunjlabs.fuga.dependency.DependencyException;

public class ExplictSingletonServiceFactory<T> implements ServiceFactory<T> {

    private final T instance;

    public ExplictSingletonServiceFactory(T instance) {
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
