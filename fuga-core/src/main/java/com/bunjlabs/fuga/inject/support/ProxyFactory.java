package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Factory;

public class ProxyFactory<T> implements InternalFactory<T> {

    private final Factory proxiedFactory;

    ProxyFactory(Factory proxiedFactory) {
        this.proxiedFactory = proxiedFactory;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        T instance = getFromProxiedFactory(dependency.getKey().getType());

        if (instance == null) {
            throw new InternalProvisionException();
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    private T getFromProxiedFactory(Class<?> type) throws InternalProvisionException {
        try {
            return (T) proxiedFactory.get(type);
        } catch (ClassCastException e) {
            throw new InternalProvisionException(e);
        }
    }
}
