package com.bunjlabs.fuga.dependency.support;

import com.bunjlabs.fuga.dependency.DependencyException;
import com.bunjlabs.fuga.dependency.Environment;
import com.bunjlabs.fuga.dependency.IocContainer;
import com.bunjlabs.fuga.dependency.annotations.Inject;
import com.bunjlabs.fuga.dependency.factory.ExplictSingletonServiceFactory;
import com.bunjlabs.fuga.dependency.factory.ServiceFactory;
import com.bunjlabs.fuga.dependency.factory.SingletonServiceFactory;
import com.bunjlabs.fuga.dependency.injector.ConstructorInjector;
import com.bunjlabs.fuga.dependency.injector.Injector;
import com.bunjlabs.fuga.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Stream;

public class DefaultIocContainer implements IocContainer {

    private final Map<Class<?>, ServiceFactory<?>> factoryRegistry = new HashMap<>();
    private final ThreadLocal<Object> prototypesCurrentlyInCreation = new ThreadLocal<>();

    @Override
    public <T> T getService(Class<T> serviceType) throws DependencyException {
        ServiceFactory<T> serviceFactory = getServiceFactory(serviceType);

        if (serviceFactory == null) {
            throw new DependencyException("ServiceFactory not found for required service type");
        }

        if (isPrototypeCurrentlyInCreation(serviceType)) {
            throw new IllegalStateException("Requested service is currently in creation.");
        }

        this.beforePrototypeCreation(serviceType);
        try {
            Object[] dependencies = resolveDependencies(serviceFactory.getDependencyTypes());

            return serviceFactory.getService(dependencies);
        } finally {
            this.afterPrototypeCreation(serviceType);
        }
    }

    @Override
    public <T> void register(Class<T> serviceType) throws DependencyException {
        ServiceFactory<T> serviceFactory = createServiceFactory(serviceType);

        factoryRegistry.put(serviceType, serviceFactory);
    }

    @Override
    public void register(Object serviceInstance) throws DependencyException {
        Class<?> serviceClass;

        if (ReflectionUtils.isJdkDynamicProxy(serviceInstance)) {
            Class<?>[] proxiedInterfaces = ReflectionUtils.getProxiedInterfaces(serviceInstance);

            if (proxiedInterfaces.length == 1) {
                serviceClass = proxiedInterfaces[0];
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            serviceClass = serviceInstance.getClass();
        }

        this.register(serviceClass, serviceInstance);
    }

    @Override
    public void register(Class serviceType, Object serviceInstance) throws DependencyException {
        ServiceFactory serviceFactory = new ExplictSingletonServiceFactory<>(serviceInstance);

        factoryRegistry.put(serviceType, serviceFactory);
    }

    @SuppressWarnings("unchecked")
    private Object[] resolveDependencies(Class[] dependencyTypes) {
        Object[] dependencies = new Object[dependencyTypes.length];

        for (int i = 0; i < dependencyTypes.length; i++) {
            dependencies[i] = this.getService(dependencyTypes[i]);
        }

        return dependencies;
    }

    @SuppressWarnings("unchecked")
    private <T> ServiceFactory<T> getServiceFactory(Class<T> serviceType) {
        return (ServiceFactory<T>) factoryRegistry.get(serviceType);
    }

    private <T> ServiceFactory<T> createServiceFactory(Class<T> serviceType) {
        MapBinder mapBinder = new DefaultMapBinder();
        Constructor<T> constructor;

        Optional<Constructor<T>> optionalConstructor = getAnnotatedConstructor(serviceType);

        if (optionalConstructor.isPresent()) {
            constructor = optionalConstructor.get();
            Inject InjectAnnotation = constructor.getAnnotation(Inject.class);

            Arrays.stream(InjectAnnotation.bindings()).forEach(b -> mapBinder.bind(b.what(), b.to()));

            Environment environment;
            try {
                environment = InjectAnnotation.environment().newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new DependencyException("Unable to instantiate environment object", ex);
            }

            environment.configure(mapBinder);
        } else {
            try {
                constructor = serviceType.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new DependencyException("No corresponding annotated or default constructor in " + serviceType.getName());
            }
        }

        Injector<T> injector = new ConstructorInjector<>(constructor);

        return new SingletonServiceFactory<>(injector, mapBinder);
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<Constructor<T>> getAnnotatedConstructor(Class<T> serviceType) {
        return Stream.of((Constructor<T>[]) serviceType.getConstructors())
                .filter((Constructor c) -> c.isAnnotationPresent(Inject.class))
                .findFirst();
    }

    private boolean isPrototypeCurrentlyInCreation(Class<?> serviceType) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        return (curVal != null &&
                (curVal.equals(serviceType) || (curVal instanceof Set && ((Set<?>) curVal).contains(serviceType))));
    }

    @SuppressWarnings("unchecked")
    private void beforePrototypeCreation(Class<?> serviceType) {
        Object curVal = this.prototypesCurrentlyInCreation.get();

        if (curVal == null) {
            this.prototypesCurrentlyInCreation.set(serviceType);
        } else if (curVal instanceof Class) {
            Set<Class> classSet = new HashSet<>(2);
            classSet.add((Class) curVal);
            classSet.add(serviceType);
            this.prototypesCurrentlyInCreation.set(classSet);
        } else {
            Set<Class> classSet = (Set<Class>) curVal;
            classSet.add(serviceType);
        }
    }

    @SuppressWarnings("unchecked")
    private void afterPrototypeCreation(Class<?> serviceType) {
        Object curVal = this.prototypesCurrentlyInCreation.get();

        if (curVal instanceof Class) {
            this.prototypesCurrentlyInCreation.remove();
        } else if (curVal instanceof Set) {
            Set<Class> classSet = (Set<Class>) curVal;
            classSet.remove(serviceType);
            if (classSet.isEmpty()) {
                this.prototypesCurrentlyInCreation.remove();
            }
        }
    }
}
