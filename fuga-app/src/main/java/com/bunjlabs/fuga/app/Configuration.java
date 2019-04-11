package com.bunjlabs.fuga.app;

public interface Configuration {

    void add(Class targetClass);

    <T> void add(Class<T> targetClass, T targetInstance);

}
