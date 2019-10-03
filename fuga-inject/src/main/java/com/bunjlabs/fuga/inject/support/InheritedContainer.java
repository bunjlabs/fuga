package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Key;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class InheritedContainer implements Container {

    private final Map<Key<?>, Binding<?>> explicitBindingsMutable = new LinkedHashMap<>();
    private final Map<Key<?>, Binding<?>> explicitBindings = Collections.unmodifiableMap(explicitBindingsMutable);
    private final Map<Class<? extends Annotation>, ScopeBinding> scopes = new HashMap<>();
    private final Container parent;

    InheritedContainer(Container parent) {
        this.parent = parent;
    }

    @Override
    public Container parent() {
        return parent;
    }

    @Override
    public boolean hasParent() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> AbstractBinding<T> getExplicitBinding(Key<T> key) {
        // key is obtained from the binding, so that key and binding inner types will always be the same
        Binding<?> binding = explicitBindings.get(key);
        return binding != null ? (AbstractBinding<T>) binding : parent.getExplicitBinding(key);
    }

    @Override
    public Map<Key<?>, Binding<?>> getExplicitBindingsLocal() {
        return explicitBindings;
    }

    @Override
    public ScopeBinding getScopeBinding(Class<? extends Annotation> annotationType) {
        ScopeBinding scopeBinding = scopes.get(annotationType);
        return scopeBinding != null ? scopeBinding : parent.getScopeBinding(annotationType);
    }

    @Override
    public void putBinding(Binding<?> binding) {
        explicitBindingsMutable.put(binding.getKey(), binding);
    }

    @Override
    public void putScopeBinding(ScopeBinding scopeBinding) {
        scopes.put(scopeBinding.getAnnotationType(), scopeBinding);
    }
}
