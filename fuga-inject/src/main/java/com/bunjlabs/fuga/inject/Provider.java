package com.bunjlabs.fuga.inject;

@FunctionalInterface
public interface Provider<T> {

    T get();
}
