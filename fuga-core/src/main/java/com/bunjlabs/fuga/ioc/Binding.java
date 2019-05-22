package com.bunjlabs.fuga.ioc;

public interface Binding {
    BindingType getBindingType();

    Class<?> getTarget();

    Object getInstance();

    Class<?> getPrototype();
}
