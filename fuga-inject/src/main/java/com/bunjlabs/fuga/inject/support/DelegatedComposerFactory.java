package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Dependency;
import com.bunjlabs.fuga.inject.Key;

class DelegatedComposerFactory<T> implements InternalFactory<T> {

    private final Key<? extends Composer> composerKey;

    DelegatedComposerFactory(Key<? extends Composer> composerKey) {
        this.composerKey = composerKey;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        var composer = context.getInjector().getInstance(composerKey);

        try {
            T instance = getFromComposer(composer, context.getRequester(), dependency.getKey());

            if (instance == null && !dependency.isNullable()) {
                throw InternalProvisionException.nullInjectedIntoNonNullableDependency(context.getRequester(), dependency);
            }

            return instance;
        } catch (RuntimeException e) {
            throw InternalProvisionException.errorInComposer(e);
        }
    }

    @SuppressWarnings("unchecked")
    private T getFromComposer(Composer composer, Key<?> requester, Key<?> requested) {
        return (T) composer.get(requester, requested);
    }
}
