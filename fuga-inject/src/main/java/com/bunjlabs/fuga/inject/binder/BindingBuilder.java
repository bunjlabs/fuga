package com.bunjlabs.fuga.inject.binder;

public interface BindingBuilder<T> extends LinkedBindingBuilder<T> {

    ScopedBindingBuilder auto();
}
