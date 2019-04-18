package com.bunjlabs.fuga.dependency.factory;

import com.bunjlabs.fuga.dependency.DependencyException;
import com.bunjlabs.fuga.dependency.support.MapBinder;
import com.bunjlabs.fuga.dependency.injector.Injector;

public class PrototypeServiceFactory<T> extends InjectableServiceFactory<T> {
    public PrototypeServiceFactory(Injector<T> injector, MapBinder mapBinder) {
        super(injector, mapBinder);
    }

    @Override
    public T getService(Object... arguments) throws DependencyException {
        return getInjector().inject(arguments);
    }
}
