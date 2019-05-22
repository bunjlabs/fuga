package com.bunjlabs.fuga.ioc.factory;

import com.bunjlabs.fuga.ioc.DependencyException;
import com.bunjlabs.fuga.ioc.injector.Injector;

public class SingletonServiceFactory<T> extends InjectableServiceFactory<T> {

    private T instance;

    public SingletonServiceFactory(Injector<T> injector) {
        super(injector);
    }

    @Override
    public T getService(Object... arguments) throws DependencyException {
        if (instance == null) {
            instance = getInjector().inject(arguments);
        }

        return instance;
    }
}
