package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Dependency;
import com.bunjlabs.fuga.inject.Key;

class ComposerInstanceFactory<T> implements InternalFactory<T> {

    private final Composer proxiedComposer;

    ComposerInstanceFactory(Composer proxiedComposer) {
        this.proxiedComposer = proxiedComposer;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        var requester = context.getRequester().getKey();
        try {
            var instance = getFromProxiedFactory(requester, dependency.getKey());

            if (instance == null && !dependency.isNullable()) {
                throw InternalProvisionException.nullInjectedIntoNonNullableDependency(requester.getType(), dependency);
            }

            if (!dependency.getKey().getType().isAssignableFrom(instance.getClass())) {
                throw new ClassCastException("Composer returned unexpected type: "
                        + instance.getClass() +
                        ". Expected: "
                        + dependency.getKey().getType());
            }

            return instance;
        } catch (RuntimeException e) {
            throw InternalProvisionException.errorInComposer(e);
        }
    }

    @SuppressWarnings("unchecked")
    private T getFromProxiedFactory(Key<?> requester, Key<?> requested) {
        return (T) proxiedComposer.get(requester, requested);
    }
}
