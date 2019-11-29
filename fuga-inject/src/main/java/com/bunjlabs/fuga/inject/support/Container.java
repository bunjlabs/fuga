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
