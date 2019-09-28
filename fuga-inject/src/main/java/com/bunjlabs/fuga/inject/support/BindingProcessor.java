package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;

interface BindingProcessor {

    <T> boolean process(Binding<T> binding);
}
