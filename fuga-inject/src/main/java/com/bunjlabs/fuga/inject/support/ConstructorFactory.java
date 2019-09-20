package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.ConfigurationException;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.ProvisionException;

import java.lang.reflect.InvocationTargetException;

public class ConstructorFactory<T> implements InternalFactory<T> {
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

        constructionContext.startConstruction();
        try {
            return construct(context, constructionContext);
        } finally {
            constructionContext.endConstruction();
        }
    }

    private T construct(InjectorContext context, ConstructionContext<T> constructionContext) throws InternalProvisionException {
        Class<?>[] parameterTypes = constructorProxy.getParameterTypes();
        Object[] parameters = resolveDependencies(context, parameterTypes);

        try {
            return constructorProxy.newInstance(parameters);
        } catch (InvocationTargetException e) {
            throw new InternalProvisionException("Error injecting constructor", e);
        } finally {
            constructionContext.endConstruction();
        }
    }

    private Object[] resolveDependencies(InjectorContext context, Class<?>[] parameterTypes) {
        Object[] objects = new Object[parameterTypes.length];

        for (int i = 0; i < objects.length; i++) {
            var parameterKey = Key.of(parameterTypes[i]);
            var provider = context.getInjector().getProviderFor(parameterKey, context);
            objects[i] = provider.get();
        }

        return objects;
    }
}
