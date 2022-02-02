package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.Dependency;
import fuga.inject.ProvisionListener;

import java.util.ArrayList;
import java.util.List;

class ListenerKeysFactory<T> implements InternalFactory<T>, Initializable {

    private final InternalFactory<T> internalFactory;
    private final List<? extends Key<? extends ProvisionListener<T>>> listenersKeys;
    private final List<DependencyInjector<? extends ProvisionListener<T>>> listenersInjectors;

    ListenerKeysFactory(InternalFactory<T> internalFactory, List<? extends Key<? extends ProvisionListener<T>>> listenersKeys) {
        this.internalFactory = internalFactory;
        this.listenersKeys = listenersKeys;
        this.listenersInjectors = new ArrayList<>(listenersKeys.size());
    }

    @Override
    public void initialize(InjectorImpl injector) {
        for (var key : listenersKeys) {
            var dependency = Dependency.of(key);
            var dependencyInjector = injector.getDependencyInjector(dependency);

            if (dependencyInjector != null) {
                listenersInjectors.add(dependencyInjector);
            }
        }
    }

    @Override
    public T get(InjectorContext context) throws InternalProvisionException {
        var instance = internalFactory.get(context);

        context.pushSource(instance);
        try {
            for (var listenerInjector : listenersInjectors) {
                var listener = listenerInjector.inject(context);

                if (listener == null) {
                    throw InternalProvisionException.nullProvisionListener(instance);
                }

                listener.provision(instance);
            }
        } catch (RuntimeException e) {
            throw InternalProvisionException.errorInProvisionListener(e);
        } finally {
            context.popSource();
        }

        return instance;
    }
}
