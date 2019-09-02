package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binder;
import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.BindingBuilder;
import com.bunjlabs.fuga.inject.Key;

import java.util.LinkedList;
import java.util.List;

public class DefaultBinder implements Binder {
    private final BindingProcessor bindingProcessor;

    public DefaultBinder(BindingProcessor bindingProcessor) {
        this.bindingProcessor = bindingProcessor;
    }

    @Override
    public <T> BindingBuilder<T> bind(Class<T> type) {
        return bind(Key.of(type));
    }

    @Override
    public <T> BindingBuilder<T> bind(Key<T> type) {
        return new DefaultBindingBuilder<>(type, bindingProcessor);
    }
}
