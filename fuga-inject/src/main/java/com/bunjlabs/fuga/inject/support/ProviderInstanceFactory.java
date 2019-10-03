package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Dependency;
import com.bunjlabs.fuga.inject.Provider;
import com.bunjlabs.fuga.util.ObjectUtils;

class ProviderInstanceFactory<T> implements InternalFactory<T> {

    private final Provider<T> provider;

    ProviderInstanceFactory(Provider<T> provider) {
        this.provider = provider;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
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

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("provider", provider)
                .toString();
    }
}
