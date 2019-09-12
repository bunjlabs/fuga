package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingBuilder;
import com.bunjlabs.fuga.inject.Key;

public abstract class AbstractBindingBuilder<T> implements BindingBuilder<T> {

    private final Key<T> key;
    private final BindingProcessor bindingProcessor;
    private AbstractBinding<T> binding;

    AbstractBindingBuilder(Key<T> key, BindingProcessor bindingProcessor) {
        this.key = key;
        this.binding = new UntargettedBinding<>(key);
        this.bindingProcessor = bindingProcessor;
    }

    protected AbstractBinding<T> getBinding() {
        return binding;
    }

    AbstractBinding<T> setBinding(AbstractBinding<T> binding) {
        bindingProcessor.process(binding);

        return binding;
    }

}
