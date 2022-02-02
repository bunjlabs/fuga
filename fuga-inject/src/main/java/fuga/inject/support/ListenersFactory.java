package fuga.inject.support;

import fuga.inject.ProvisionListener;

import java.util.List;

class ListenersFactory<T> implements InternalFactory<T> {

    private final InternalFactory<T> internalFactory;
    private final List<ProvisionListener<T>> listeners;

    ListenersFactory(InternalFactory<T> internalFactory, List<ProvisionListener<T>> listeners) {
        this.internalFactory = internalFactory;
        this.listeners = listeners;
    }

    @Override
    public T get(InjectorContext context) throws InternalProvisionException {
        var instance = internalFactory.get(context);

        try {
            for (var listener : listeners) {
                listener.provision(instance);
            }
        } catch (RuntimeException e) {
            throw InternalProvisionException.errorInProvisionListener(e);
        }

        return instance;
    }
}
