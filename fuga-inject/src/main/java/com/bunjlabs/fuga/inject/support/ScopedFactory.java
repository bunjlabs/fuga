package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Dependency;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;
import com.bunjlabs.fuga.inject.Scope;

class ScopedFactory<T> implements InternalFactory<T> {

    private final InternalFactory<T> internalFactory;
    private final Scope scope;

    ScopedFactory(InternalFactory<T> internalFactory, Scope scope) {
        this.internalFactory = internalFactory;
        this.scope = scope;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        @SuppressWarnings("unchecked")
        var key = (Key<T>) dependency.getKey();

        Provider<T> provider = () -> {
            try {
                return internalFactory.get(context, dependency);
            } catch (InternalProvisionException e) {
                // TODO: error processing
            }
            return null;
        };

        Provider<T> scopedProvider = this.scope.scope(key, provider);

        var instance = scopedProvider.get();
        if (instance == null && !dependency.isNullable()) {
            throw InternalProvisionException.nullInjectedIntoNonNullableDependency(context.getRequester(), dependency);
        }

        return instance;
    }
}
