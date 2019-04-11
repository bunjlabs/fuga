package com.bunjlabs.fuga.dependency.factory;

import com.bunjlabs.fuga.dependency.DependencyException;
import com.bunjlabs.fuga.dependency.MapBinder;
import com.bunjlabs.fuga.dependency.injector.Injector;

public class SingletonServiceFactory<T> extends InjectableServiceFactory<T> {

    private T instance;

    public SingletonServiceFactory(Injector<T> injector, MapBinder mapBinder) {
        super(injector, mapBinder);
    }

    @Override
    public T getService(Object... arguments) throws DependencyException {
        if (instance == null) {
            instance = getInjector().inject(arguments);
        }

        return instance;
    }
}
