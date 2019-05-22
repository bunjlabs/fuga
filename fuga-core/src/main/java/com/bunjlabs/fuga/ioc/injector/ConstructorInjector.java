package com.bunjlabs.fuga.ioc.injector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorInjector<T> implements Injector<T> {

    private final Constructor<T> constructor;

    public ConstructorInjector(Constructor<T> constructor) {
        this.constructor = constructor;
    }

    @Override
    public T inject(Object[] arguments) throws InjectException {
        try {
            return constructor.newInstance(arguments);
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new InjectException("Unable to inject dependencies with annotated constructor", ex);
        }
    }

    @Override
    public Class[] getRequiredTypes() {
        return this.constructor.getParameterTypes();
    }
}