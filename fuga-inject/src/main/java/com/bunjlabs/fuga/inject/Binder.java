package com.bunjlabs.fuga.inject;

import com.bunjlabs.fuga.inject.binder.BindingBuilder;

import java.lang.annotation.Annotation;

public interface Binder {

    void bindScope(Class<? extends Annotation> annotationType, Scope scope);

    <T> BindingBuilder<T> bind(Class<T> type);

    <T> BindingBuilder<T> bind(Key<T> type);
}
