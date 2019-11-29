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
import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.bindings.*;

abstract class AbstractBindingVisitor<T, V> implements BindingVisitor<T, V> {

    final Key<T> key;
    final Scoping scoping;

    AbstractBindingVisitor(AbstractBinding<T> binding) {
        this.key = binding.getKey();
        this.scoping = binding.getScoping();
    }

    private V visitOther(Binding<? extends T> binding) {
        return null;
    }

    @Override
    public V visit(InstanceBinding<? extends T> binding) {
        return visitOther(binding);
    }

    @Override
    public V visit(ConstructorBinding<? extends T> binding) {
        return visitOther(binding);
    }

    @Override
    public V visit(ProviderKeyBinding<? extends T> binding) {
        return visitOther(binding);
    }

    @Override
    public V visit(ProviderBinding<? extends T> binding) {
        return visitOther(binding);
    }

    @Override
    public V visit(ComposerKeyBinding<? extends T> binding) {
        return visitOther(binding);
    }

    @Override
    public V visit(ComposerBinding<? extends T> binding) {
        return visitOther(binding);
    }

    @Override
    public V visit(LinkedKeyBinding<? extends T> binding) {
        return visitOther(binding);
    }

    @Override
    public V visit(AutoBinding<? extends T> binding) {
        return visitOther(binding);
    }

    @Override
    public V visit(UntargettedBinding<? extends T> binding) {
        return visitOther(binding);
    }

}
