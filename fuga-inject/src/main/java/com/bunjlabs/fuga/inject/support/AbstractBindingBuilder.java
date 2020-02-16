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

import java.util.List;

abstract class AbstractBindingBuilder<T> {

    private final List<AbstractBinding<?>> bindingList;
    private final int position;
    private AbstractBinding<T> binding;

    AbstractBindingBuilder(Key<T> key, List<AbstractBinding<?>> bindingList) {
        this.binding = new UntargettedBindingImpl<>(key, Scoping.UNSCOPED);
        this.bindingList = bindingList;
        this.position = bindingList.size();
        this.bindingList.add(this.position, this.binding);
    }

    protected AbstractBinding<T> getBinding() {
        return binding;
    }

    void setBinding(AbstractBinding<T> binding) {
        this.bindingList.set(position, binding);
        this.binding = binding;
    }
}

