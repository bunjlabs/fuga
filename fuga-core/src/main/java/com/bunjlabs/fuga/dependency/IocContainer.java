package com.bunjlabs.fuga.dependency;

public interface IocContainer {

    <T> T getService(Class<T> requiredType) throws DependencyException;

    <T> void register(Class<T> serviceType) throws DependencyException;

    void register(Class serviceType, Object serviceInstance) throws DependencyException;
}
