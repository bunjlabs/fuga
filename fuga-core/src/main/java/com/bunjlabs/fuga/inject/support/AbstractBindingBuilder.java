package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.BindingBuilder;
import com.bunjlabs.fuga.inject.Key;

import java.util.List;

public abstract class AbstractBindingBuilder<T> implements BindingBuilder<T> {

    private final Key<T> key;
    private final List<Binding<?>> bindings;
    private int position;
    private AbstractBinding<T> binding;

    AbstractBindingBuilder(Key<T> key, List<Binding<?>> bindings) {
        this.key = key;
        this.bindings = bindings;
        this.position = bindings.size();
        this.binding = new UntargettedBinding<>(key);
        this.bindings.add(this.position, this.binding);
    }

    protected AbstractBinding<T> getBinding() {
        return binding;
    }

    AbstractBinding<T> setBinding(AbstractBinding<T> binding) {
        this.binding = binding;
        this.bindings.set(position, binding);

        return binding;
    }

}
