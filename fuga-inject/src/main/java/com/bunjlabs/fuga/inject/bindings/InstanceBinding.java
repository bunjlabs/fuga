package com.bunjlabs.fuga.inject.bindings;

import com.bunjlabs.fuga.inject.Binding;

public interface InstanceBinding<T> extends Binding<T> {

    T getInstance();
}
