/*
 * Copyright 2019 Bunjlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InheritedContainer implements Container {

    private final Map<Key<?>, List<AbstractBinding<?>>> explicitBindingsMutable = new LinkedHashMap<>();
    private final Map<Key<?>, List<AbstractBinding<?>>> explicitBindings = Collections.unmodifiableMap(explicitBindingsMutable);
    private final Map<Class<? extends Annotation>, ScopeBinding> scopes = new LinkedHashMap<>();
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
    public <T> AbstractBinding<T> getExplicitBinding(Key<T> key) {
        var binding = doGetBinding(key);
        return binding != null ? binding : parent.getExplicitBinding(key);
    }

    @Override
    public <T> List<AbstractBinding<T>> getAllExplicitBindings(Key<T> key) {
        var localBindings = doGetAllBindings(key);
        return Stream.of(localBindings, parent.getAllExplicitBindings(key))
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ScopeBinding getScopeBinding(Class<? extends Annotation> annotationType) {
        ScopeBinding scopeBinding = scopes.get(annotationType);
        return scopeBinding != null ? scopeBinding : parent.getScopeBinding(annotationType);
    }

    @Override
    public void putBinding(AbstractBinding<?> binding) {
        doPutBinding(binding);
    }

    @Override
    public void putScopeBinding(ScopeBinding scopeBinding) {
        scopes.put(scopeBinding.getAnnotationType(), scopeBinding);
    }

    @SuppressWarnings("unchecked")
    private <T> AbstractBinding<T> doGetBinding(Key<T> key) {
        var bindings = explicitBindings.get(key);
        // key is obtained from the binding, so that key and binding inner types will always be the same
        return bindings != null && !bindings.isEmpty() ? (AbstractBinding<T>) bindings.get(bindings.size() - 1) : null;
    }

    @SuppressWarnings("unchecked")
    private <T> List<AbstractBinding<T>> doGetAllBindings(Key<T> key) {
        var bindings = explicitBindings.get(key);

        if (bindings == null) {
            return Collections.emptyList();
        }

        var localBindings = new ArrayList<AbstractBinding<T>>(bindings.size());
        for (AbstractBinding<?> binding : bindings) {
            // key is obtained from the binding, so that key and binding inner types will always be the same
            localBindings.add((AbstractBinding<T>) binding);
        }

        return Collections.unmodifiableList(localBindings);
    }

    private void doPutBinding(AbstractBinding<?> binding) {
        List<AbstractBinding<?>> bindings;
        var key = binding.getKey();

        if (explicitBindingsMutable.containsKey(key)) {
            bindings = explicitBindingsMutable.get(key);
        } else {
            bindings = new ArrayList<>();
            explicitBindingsMutable.put(key, bindings);
        }

        bindings.add(binding);
    }


}
