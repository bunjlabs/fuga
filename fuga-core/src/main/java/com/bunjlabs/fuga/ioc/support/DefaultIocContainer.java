package com.bunjlabs.fuga.ioc.support;

import com.bunjlabs.fuga.ioc.*;
import com.bunjlabs.fuga.ioc.factory.ExplicitSingletonServiceFactory;
import com.bunjlabs.fuga.ioc.factory.ServiceFactory;
import com.bunjlabs.fuga.ioc.factory.SingletonServiceFactory;
import com.bunjlabs.fuga.ioc.injector.ConstructorInjector;
import com.bunjlabs.fuga.ioc.injector.Injector;
import com.bunjlabs.fuga.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Stream;

public class DefaultIocContainer implements IocContainer {

    private final Map<Class<?>, ServiceFactory<?>> factoryRegistry = new HashMap<>();
    private final ThreadLocal<Object> prototypesCurrentlyInCreation = new ThreadLocal<>();

    @Override
    public <T> T getService(Class<T> type) throws DependencyException {
        ServiceFactory<T> serviceFactory = getServiceFactory(type);

        if (serviceFactory == null) {
            throw new DependencyException("Requested service not found: " + type);
        }

        if (isPrototypeCurrentlyInCreation(type)) {
            throw new IllegalStateException("Requested service is currently in creation: " + type);
        }

        this.beforePrototypeCreation(type);
        try {
            Object[] dependencies = resolveDependencies(serviceFactory.getDependencyTypes());

            return serviceFactory.getService(dependencies);
        } finally {
            this.afterPrototypeCreation(type);
        }
    }

    @Override
    public <T> void register(Class<T> type) throws DependencyException {
        register(type, type);
    }

    @Override
    public void register(Object instance) throws DependencyException {
        Class<?> serviceClass;

        if (ReflectionUtils.isJdkDynamicProxy(instance)) {
            Class<?>[] proxiedInterfaces = ReflectionUtils.getProxiedInterfaces(instance);

            if (proxiedInterfaces.length == 1) {
                serviceClass = proxiedInterfaces[0];
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            serviceClass = instance.getClass();
        }

        this.register(serviceClass, instance);
    }

    @Override
    public void register(Class type, Class<?> servicePrototype) throws DependencyException {
        ServiceFactory serviceFactory = createServiceFactory(servicePrototype);

        factoryRegistry.put(type, serviceFactory);
    }

    @Override
    public void register(Class serviceType, Object serviceInstance) throws DependencyException {
        ServiceFactory serviceFactory = new ExplicitSingletonServiceFactory<>(serviceInstance);

        factoryRegistry.put(serviceType, serviceFactory);
    }

    public void configureModule(Module module) throws Exception {
        DefaultConfiguration configuration = new DefaultConfiguration();

        module.configure(configuration);

        configuration.getBindings().forEach(b -> {
            if (b.getBindingType() == BindingType.INSTANCE) {
                register(b.getTarget(), b.getInstance());
            } else {
                register(b.getTarget(), b.getPrototype());
            }
        });
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
        Constructor<T> constructor;

        Optional<Constructor<T>> optionalAnnotatedConstructor = getAnnotatedConstructor(serviceType);

        if (optionalAnnotatedConstructor.isPresent()) {
            constructor = optionalAnnotatedConstructor.get();
        } else {
            try {
                constructor = serviceType.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new DependencyException("No corresponding annotated or default constructor in " + serviceType.getName());
            }
        }

        Injector<T> injector = new ConstructorInjector<>(constructor);

        return new SingletonServiceFactory<>(injector);
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
