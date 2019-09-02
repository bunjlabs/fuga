package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;

public class DelegatedComposerFactory<T> implements InternalFactory<T> {

    private final Key<? extends Composer> composerKey;

    DelegatedComposerFactory(Key<? extends Composer> composerKey) {
        this.composerKey = composerKey;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        Composer composer = context.getInjector().getInstance(composerKey);

        T instance = getFromComposer(composer, dependency.getKey().getType());

        if (instance == null) {
            throw new InternalProvisionException();
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    private T getFromComposer(Composer composer, Class<?> type) throws InternalProvisionException {
        try {
            return (T) composer.get(type);
        } catch (ClassCastException e) {
            throw new InternalProvisionException(e);
        }
    }
}
