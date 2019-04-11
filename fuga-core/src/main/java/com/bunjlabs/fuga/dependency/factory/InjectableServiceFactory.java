package com.bunjlabs.fuga.dependency.factory;

import com.bunjlabs.fuga.dependency.MapBinder;
import com.bunjlabs.fuga.dependency.injector.Injector;

public abstract class InjectableServiceFactory<T> implements ServiceFactory<T> {

    private final Injector<T> injector;
    private final MapBinder mapBinder;

    InjectableServiceFactory(Injector<T> injector, MapBinder mapBinder) {
        this.injector = injector;
        this.mapBinder = mapBinder;
    }

    @Override
    public Class[] getDependencyTypes() {
        Class[] originalArgumentTypes = injector.getRequiredTypes();
        return this.bindArgumentTypes(originalArgumentTypes);
    }

    Injector<T> getInjector() {
        return injector;
    }


    private Class[] bindArgumentTypes(Class[] input) {
        Class[] bindTypes = new Class[input.length];

        for (int i = 0; i < input.length; i++) {
            bindTypes[i] = mapBinder.getBindingFor(input[i]);
        }

        return bindTypes;
    }
}
