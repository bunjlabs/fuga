package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;

public class DelegatedKeyFactory<T> implements InternalFactory<T> {

    private final Key<? extends T> targetKey;

    DelegatedKeyFactory(Key<? extends T> targetKey) {
        this.targetKey = targetKey;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        InternalFactory<? extends T> delegatedFactory = context.getInjector().getInternalFactory(targetKey);

        return delegatedFactory.get(context, dependency);
    }
}
