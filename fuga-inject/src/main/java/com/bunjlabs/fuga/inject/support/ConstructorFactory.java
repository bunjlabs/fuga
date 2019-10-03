package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Dependency;

class ConstructorFactory<T> implements InternalFactory<T> {

    private final ConstructorInjector<T> constructorInjector;

    ConstructorFactory(ConstructorInjector<T> constructorInjector) {
        this.constructorInjector = constructorInjector;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        var instance = constructorInjector.construct(context);

        if (instance == null && !dependency.isNullable()) {
            throw InternalProvisionException.nullInjectedIntoNonNullableDependency(context.getDependency(), dependency);
        }

        return instance;
    }
}
