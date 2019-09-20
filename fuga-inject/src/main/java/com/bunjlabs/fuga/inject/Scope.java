package com.bunjlabs.fuga.inject;

public interface Scope {

    <T> Provider<T> scope(Key<T> key, Provider<T> provider);
}
