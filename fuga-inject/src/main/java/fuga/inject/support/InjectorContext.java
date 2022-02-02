/*
 * Copyright 2019-2021 Bunjlabs
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

package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.Binding;
import fuga.inject.Dependency;
import fuga.inject.Injector;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

class InjectorContext {

    private final InjectorImpl injector;
    private final List<Dependency<?>> dependencyStack;
    private final List<AbstractBinding<?>> bindingStack;
    private final List<Object> sourceStack;
    private final Map<Object, ConstructionContext<?>> constructionContexts = new IdentityHashMap<>();

    InjectorContext(InjectorImpl injector) {
        this.injector = injector;
        this.dependencyStack = Collections.synchronizedList(new ArrayList<>(5));
        this.bindingStack = Collections.synchronizedList(new ArrayList<>(5));
        this.sourceStack = Collections.synchronizedList(new ArrayList<>(5));
        pushDependency(Dependency.of(Injector.class));
    }

    InjectorImpl getInjector() {
        return injector;
    }

    Dependency<?> getDependency() {
        var size = dependencyStack.size();
        return size > 0 ? dependencyStack.get(size - 1) : null;
    }

    Dependency<?> getRequester() {
        var size = dependencyStack.size();
        return size > 1 ? dependencyStack.get(size - 2) : null;
    }

    <T> T getAttribute(Key<T> key) {
        for (int i = bindingStack.size() - 1; i >= 0; i--) {
            var attribute = bindingStack.get(i).getAttribute(key);
            if (attribute != null) {
                return attribute;
            }
        }

        return null;
    }

    Object getSource() {
        var size = sourceStack.size();
        return size > 0 ? sourceStack.get(size - 1) : null;
    }

    void pushDependency(Dependency<?> dependency) {
        dependencyStack.add(dependency);
    }

    void popDependency() {
        var size = dependencyStack.size();
        if (size > 0) {
            dependencyStack.remove(size - 1);
        }
    }

    void pushBinding(AbstractBinding<?> binding) {
        bindingStack.add(binding);
    }

    void popBinding() {
        var size = bindingStack.size();
        if (size > 0) {
            bindingStack.remove(size - 1);
        }
    }


    void pushSource(Object source) {
        sourceStack.add(source);
    }

    void popSource() {
        var size = sourceStack.size();
        if (size > 0) {
            sourceStack.remove(size - 1);
        }
    }

    @SuppressWarnings("unchecked")
    <T> AbstractBinding<T> findBinding(Key<T> key) {
        for (int i = bindingStack.size() - 1; i >= 0; i--) {
            var binding = bindingStack.get(i);
            if (binding.getKey().equals(key)) {
                return (AbstractBinding<T>) binding;
            }
        }

        return null;
    }

    <T> ConstructionContext<T> getConstructionContext(Object key) {
        @SuppressWarnings("unchecked")
        var constructionContext = (ConstructionContext<T>) constructionContexts.get(key);

        if (constructionContext == null) {
            constructionContext = new ConstructionContext<>();
            constructionContexts.put(key, constructionContext);
        }

        return constructionContext;
    }
}