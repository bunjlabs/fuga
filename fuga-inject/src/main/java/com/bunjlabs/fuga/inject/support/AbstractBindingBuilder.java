package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;

import java.util.List;

abstract class AbstractBindingBuilder<T> {

    private final Key<T> key;
    private final List<AbstractBinding<?>> bindingList;
    private final int position;
    private AbstractBinding<T> binding;

    AbstractBindingBuilder(Key<T> key, List<AbstractBinding<?>> bindingList) {
        this.key = key;
        this.binding = new UntargettedBindingImpl<>(key, Scoping.UNSCOPED);
        this.bindingList = bindingList;
        this.position = bindingList.size();
        bindingList.add(this.position, this.binding);
    }

    protected AbstractBinding<T> getBinding() {
        return binding;
    }

    void setBinding(AbstractBinding<T> binding) {
        bindingList.set(position, binding);
    }
}

