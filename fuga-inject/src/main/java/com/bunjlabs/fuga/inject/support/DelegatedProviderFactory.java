package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Dependency;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;

class DelegatedProviderFactory<T> implements InternalFactory<T> {

    private final Key<? extends Provider<? extends T>> providerKey;

    DelegatedProviderFactory(Key<? extends Provider<? extends T>> providerKey) {
        this.providerKey = providerKey;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        Provider<? extends T> provider = context.getInjector().getInstance(providerKey);

        try {
            var instance = provider.get();

            if (instance == null && !dependency.isNullable()) {
                throw InternalProvisionException.nullInjectedIntoNonNullableDependency(
                        context.getDependency().getKey().getType(), dependency);
            }

            return instance;
        } catch (RuntimeException e) {
            throw InternalProvisionException.errorInProvider(e);
        }
    }
}
