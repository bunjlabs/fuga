package com.bunjlabs.fuga.dependency.factory;

import com.bunjlabs.fuga.dependency.DependencyException;

public interface ServiceFactory<T> {

    T getService(Object... arguments) throws DependencyException;

    Class[] getDependencyTypes();
}
