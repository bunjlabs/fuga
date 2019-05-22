package com.bunjlabs.fuga.ioc.factory;

import com.bunjlabs.fuga.ioc.DependencyException;

public interface ServiceFactory<T> {

    T getService(Object... arguments) throws DependencyException;

    Class[] getDependencyTypes();
}
