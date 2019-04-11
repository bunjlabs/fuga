package com.bunjlabs.fuga.dependency;

public interface DependencyFactory<T> {

    T getObject();
}
