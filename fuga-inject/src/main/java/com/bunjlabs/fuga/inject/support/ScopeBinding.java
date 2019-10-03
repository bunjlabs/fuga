package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Scope;

import java.lang.annotation.Annotation;

class ScopeBinding {

    private final Class<? extends Annotation> annotationType;
    private final Scope scope;

    public ScopeBinding(Class<? extends Annotation> annotationType, Scope scope) {
        this.annotationType = annotationType;
        this.scope = scope;
    }

    public Class<? extends Annotation> getAnnotationType() {
        return annotationType;
    }

    public Scope getScope() {
        return scope;
    }


}
