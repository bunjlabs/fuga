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

import fuga.inject.Dependency;
import fuga.inject.Injector;

import java.util.*;

class InjectorContext {

    private final InjectorImpl injector;
    private final List<Dependency<?>> dependencyStack;
    private final Map<Object, ConstructionContext<?>> constructionContexts = new IdentityHashMap<>();

    InjectorContext(InjectorImpl injector) {
        this.injector = injector;
        this.dependencyStack = Collections.synchronizedList(new ArrayList<>());
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

    void pushDependency(Dependency<?> dependency) {
        dependencyStack.add(dependency);
    }

    void popDependency() {
        var size = dependencyStack.size();
        if (size > 0) {
            dependencyStack.remove(size - 1);
        }
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
