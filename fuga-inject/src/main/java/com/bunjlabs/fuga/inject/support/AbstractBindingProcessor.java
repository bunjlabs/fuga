package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;

abstract class AbstractBindingProcessor implements BindingProcessor {
    private final Container container;

    AbstractBindingProcessor(Container container) {
        this.container = container;
    }

    <T> void putBinding(Binding<T> binding) {
        container.putBinding(binding);
    }
}
