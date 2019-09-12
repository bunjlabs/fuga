package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;

public interface BindingProcessor {

    <T> boolean process(Binding<T> binding);
}
