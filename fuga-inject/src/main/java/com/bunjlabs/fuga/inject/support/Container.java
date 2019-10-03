package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Key;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

interface Container {

    Container EMPTY = new Container() {
        @Override
        public Container parent() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasParent() {
            return false;
        }

        @Override
        public <T> AbstractBinding<T> getExplicitBinding(Key<T> key) {
            return null;
        }

        @Override
        public Map<Key<?>, Binding<?>> getExplicitBindingsLocal() {
            return Collections.emptyMap();
        }

        @Override
        public ScopeBinding getScopeBinding(Class<? extends Annotation> annotationType) {
            return null;
        }

        @Override
        public void putBinding(Binding<?> binding) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putScopeBinding(ScopeBinding scopeBinding) {
            throw new UnsupportedOperationException();
        }
    };

    Container parent();

    boolean hasParent();

    <T> AbstractBinding<T> getExplicitBinding(Key<T> key);

    Map<Key<?>, Binding<?>> getExplicitBindingsLocal();

    ScopeBinding getScopeBinding(Class<? extends Annotation> annotationType);

    void putBinding(Binding<?> binding);

    void putScopeBinding(ScopeBinding scopeBinding);
}
