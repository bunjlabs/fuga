package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.InjectionPoint;

import java.lang.reflect.InvocationTargetException;

class ConstructorInjector<T> {

    private final InjectionPoint injectionPoint;
    private final ConstructionProxy<T> constructionProxy;

    ConstructorInjector(InjectionPoint injectionPoint, ConstructionProxy<T> constructionProxy) {
        this.injectionPoint = injectionPoint;
        this.constructionProxy = constructionProxy;
    }

    T construct(InjectorContext context) throws InternalProvisionException {
        ConstructionContext<T> constructionContext = context.getConstructionContext(this);

        if (constructionContext.isConstructing()) {
            throw InternalProvisionException.circularDependencies(context.getDependency().getKey().getType());
        }

        return construct(context, constructionContext);
    }

    private T construct(InjectorContext context, ConstructionContext<T> constructionContext) throws InternalProvisionException {
        constructionContext.startConstruction();
        try {
            var parameters = resolveDependencies(context);
            return constructionProxy.newInstance(parameters);
        } catch (InvocationTargetException e) {
            throw InternalProvisionException.errorInjectingConstructor(e);
        } finally {
            constructionContext.endConstruction();
        }
    }

    private Object[] resolveDependencies(InjectorContext context) throws InternalProvisionException {
        var dependencies = injectionPoint.getDependencies();
        var objects = new Object[dependencies.size()];

        for (int i = 0; i < objects.length; i++) {
            var injector = context.getInjector().getDependencyInjector(dependencies.get(i));
            objects[i] = injector.inject(context);
        }

        return objects;
    }
}
