package com.bunjlabs.fuga.dependency;

public interface IocContainer {

    <T> T getService(Class<T> serviceType) throws DependencyException;

    <T> void register(Class<T> serviceType) throws DependencyException;

    void register(Object serviceInstance) throws DependencyException;

    void register(Class serviceType, Object serviceInstance) throws DependencyException;

    //<T> T resolveDependenciesFor(Class<T> serviceType);
}
