package com.bunjlabs.fuga.ioc.factory;

import com.bunjlabs.fuga.ioc.injector.Injector;

public abstract class InjectableServiceFactory<T> implements ServiceFactory<T> {

    private final Injector<T> injector;

    InjectableServiceFactory(Injector<T> injector) {
        this.injector = injector;
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
            bindTypes[i] = input[i];
        }

        return bindTypes;
    }
}
