package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;

public class DelegatedProviderFactory<T> implements InternalFactory<T> {

    private final Key<? extends Provider<? extends T>> providerKey;

    DelegatedProviderFactory(Key<? extends Provider<? extends T>> providerKey) {
        this.providerKey = providerKey;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        Provider<? extends T> instance = context.getInjector().getInstance(providerKey);

        return instance.get();
    }
}
