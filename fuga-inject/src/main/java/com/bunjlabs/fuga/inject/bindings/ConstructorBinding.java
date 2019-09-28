package com.bunjlabs.fuga.inject.bindings;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.InjectionPoint;

public interface ConstructorBinding<T> extends Binding<T> {

    InjectionPoint getInjectionPoint();
}
