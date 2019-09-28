package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Scope;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class InheritedContainer implements Container {

    private final Map<Key<?>, Binding<?>> explicitBindingsMutable = new LinkedHashMap<>();
    private final Map<Key<?>, Binding<?>> explicitBindings = Collections.unmodifiableMap(explicitBindingsMutable);
    private final Map<Class<? extends Annotation>, Scope> scopes = new HashMap();
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
    public Scope getScope(Class<? extends Annotation> annotationType) {
        Scope scope = scopes.get(annotationType);
        return scope != null ? scope : parent.getScope(annotationType);
    }

    @Override
    public void putBinding(Binding<?> binding) {
        explicitBindingsMutable.put(binding.getKey(), binding);
    }

    @Override
    public void putScopeBinding(Class<? extends Annotation> annotationType, Scope scope) {
        scopes.put(annotationType, scope);
    }
}
