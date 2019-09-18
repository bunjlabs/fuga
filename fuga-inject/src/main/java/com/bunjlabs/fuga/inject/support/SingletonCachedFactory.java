package com.bunjlabs.fuga.inject.support;

public class SingletonCachedFactory<T> implements InternalFactory<T> {
    private final InternalFactory<T> internalFactory;
    private T instance;

    SingletonCachedFactory(InternalFactory<T> internalFactory) {
        this.internalFactory = internalFactory;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        if (instance == null) {
            instance = internalFactory.get(context, dependency);
        }

        return instance;
    }
}
