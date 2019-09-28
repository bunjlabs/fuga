package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Dependency;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.ProvisionException;

import java.lang.reflect.InvocationTargetException;

class ConstructorFactory<T> implements InternalFactory<T> {

    private final ConstructorProxy<T> constructorProxy;

    ConstructorFactory(ConstructorProxy<T> constructorProxy) {
        this.constructorProxy = constructorProxy;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        ConstructionContext<T> constructionContext = context.getConstructionContext(this);

        if (constructionContext.isConstructing()) {
            throw new ProvisionException("Circular construction " + dependency);
        }

        context.enterRequester(constructorProxy.getType());
        constructionContext.startConstruction();
        try {
            var instance = construct(context, constructionContext);
            if (instance == null && !dependency.isNullable()) {
                throw InternalProvisionException.nullInjectedIntoNonNullableDependency(context.getRequester(), dependency);
            }
            return instance;
        } finally {
            constructionContext.endConstruction();
            context.exitRequester();
        }
    }

    private T construct(InjectorContext context, ConstructionContext<T> constructionContext) throws InternalProvisionException {
        Key<?>[] parameterTypes = constructorProxy.getParameterTypes();
        Object[] parameters = resolveDependencies(context, parameterTypes);

        try {
            return constructorProxy.newInstance(parameters);
        } catch (InvocationTargetException e) {
            throw InternalProvisionException.errorInjectingConstructor(e);
        } finally {
            constructionContext.endConstruction();
        }
    }

    private Object[] resolveDependencies(InjectorContext context, Key<?>[] parameterTypes) throws InternalProvisionException {
        Object[] objects = new Object[parameterTypes.length];

        for (int i = 0; i < objects.length; i++) {
            var factory = context.getInjector().getInternalFactory(parameterTypes[i]);
            objects[i] = factory.get(context, Dependency.of(parameterTypes[i]));
        }

        return objects;
    }
}
