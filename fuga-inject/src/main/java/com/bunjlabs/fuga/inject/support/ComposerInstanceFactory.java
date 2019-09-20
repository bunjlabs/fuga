package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;

public class ComposerInstanceFactory<T> implements InternalFactory<T> {

    private final Composer proxiedComposer;

    ComposerInstanceFactory(Composer proxiedComposer) {
        this.proxiedComposer = proxiedComposer;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        var requester = context.getRequester();
        var instance = getFromProxiedFactory(requester, dependency.getKey());

        if (instance == null) {
            throw new InternalProvisionException("Composer returned null");
        }

        if (!dependency.getKey().getType().isAssignableFrom(instance.getClass())) {
            throw new InternalProvisionException("Composer returned unexpected type: "
                    + instance.getClass() +
                    ". Expected: "
                    + dependency.getKey().getType());
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    private T getFromProxiedFactory(Key<?> requester, Key<?> requested) throws InternalProvisionException {
        try {
            return (T) proxiedComposer.get(requester, requested);
        } catch (ClassCastException e) {
            throw new InternalProvisionException(e);
        }
    }
}
