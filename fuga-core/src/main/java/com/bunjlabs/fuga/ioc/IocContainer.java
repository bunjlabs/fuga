package com.bunjlabs.fuga.ioc;

public interface IocContainer {

    <T> T getService(Class<T> type) throws DependencyException;

    <T> void register(Class<T> type) throws DependencyException;

    void register(Object instance) throws DependencyException;

    void register(Class type, Class<?> servicePrototype) throws DependencyException;

    void register(Class serviceType, Object serviceInstance) throws DependencyException;
}
