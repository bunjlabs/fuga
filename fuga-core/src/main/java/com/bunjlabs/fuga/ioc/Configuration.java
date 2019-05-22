package com.bunjlabs.fuga.ioc;

public interface Configuration {

    void add(Class<?> targetClass);

    void add(Object instance);

    void add(Class<?> targetInterface, Class<?> implementation);

    void add(Class<?> targetClass, Object implementationInstance);

}
