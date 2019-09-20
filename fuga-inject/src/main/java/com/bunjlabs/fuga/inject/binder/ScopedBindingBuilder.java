package com.bunjlabs.fuga.inject.binder;

import com.bunjlabs.fuga.inject.Scope;

import java.lang.annotation.Annotation;

public interface ScopedBindingBuilder {

    void in(Class<? extends Annotation> scopeAnnotation);

    void in(Scope scope);

}
