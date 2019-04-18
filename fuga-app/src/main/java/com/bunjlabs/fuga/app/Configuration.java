package com.bunjlabs.fuga.app;

public interface Configuration {

    void add(Class targetClass);

    void add(Object targetInstance);

    <T> void add(Class<T> targetClass, T targetInstance);

}
