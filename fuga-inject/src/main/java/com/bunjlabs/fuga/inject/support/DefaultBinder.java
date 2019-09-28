package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binder;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.binder.BindingBuilder;

import java.util.ArrayList;
import java.util.List;

class DefaultBinder implements Binder {

    private final List<AbstractBinding<?>> bindingList = new ArrayList<>();

    DefaultBinder() {
    }

    @Override
    public <T> BindingBuilder<T> bind(Class<T> type) {
        return bind(Key.of(type));
    }

    @Override
    public <T> BindingBuilder<T> bind(Key<T> type) {
        return new DefaultBindingBuilder<>(type, bindingList);
    }

    public List<AbstractBinding<?>> getBindings() {
        return bindingList;
    }
}
