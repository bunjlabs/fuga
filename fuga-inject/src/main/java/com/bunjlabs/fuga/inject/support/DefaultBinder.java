package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binder;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Scope;
import com.bunjlabs.fuga.inject.binder.BindingBuilder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

class DefaultBinder implements Binder {

    private final List<AbstractBinding<?>> bindings = new ArrayList<>();
    private final List<ScopeBinding> scopeBindings = new ArrayList<>();

    DefaultBinder() {
    }

    @Override
    public void bindScope(Class<? extends Annotation> annotationType, Scope scope) {
        scopeBindings.add(new ScopeBinding(annotationType, scope));
    }

    @Override
    public <T> BindingBuilder<T> bind(Class<T> type) {
        return bind(Key.of(type));
    }

    @Override
    public <T> BindingBuilder<T> bind(Key<T> type) {
        return new DefaultBindingBuilder<>(type, bindings);
    }

    public List<AbstractBinding<?>> getBindings() {
        return bindings;
    }

    public List<ScopeBinding> getScopeBindings() {
        return scopeBindings;
    }
}
