package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Composer;

public class ProxyFactory<T> implements InternalFactory<T> {

    private final Composer proxiedComposer;

    ProxyFactory(Composer proxiedComposer) {
        this.proxiedComposer = proxiedComposer;
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
            return (T) proxiedComposer.get(type);
        } catch (ClassCastException e) {
            throw new InternalProvisionException(e);
        }
    }
}
