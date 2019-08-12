package com.bunjlabs.fuga.ioc.factory;

import com.bunjlabs.fuga.ioc.injector.Injector;

public abstract class InjectableServiceFactory<T> implements ServiceFactory<T> {

    private final Injector<T> injector;

    InjectableServiceFactory(Injector<T> injector) {
        this.injector = injector;
    }

    @Override
    public Class[] getDependencyTypes() {
        var originalArgumentTypes = injector.getRequiredTypes();
        return this.bindArgumentTypes(originalArgumentTypes);
    }

    Injector<T> getInjector() {
        return injector;
    }


    private Class[] bindArgumentTypes(Class[] input) {
        var bindTypes = new Class[input.length];

        System.arraycopy(input, 0, bindTypes, 0, input.length);

        return bindTypes;
    }
}
