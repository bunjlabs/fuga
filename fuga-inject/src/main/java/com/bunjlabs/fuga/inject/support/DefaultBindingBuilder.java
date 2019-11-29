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

import com.bunjlabs.fuga.inject.*;
import com.bunjlabs.fuga.inject.binder.BindingBuilder;
import com.bunjlabs.fuga.inject.binder.ScopedBindingBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;

class DefaultBindingBuilder<T> extends AbstractBindingBuilder<T> implements BindingBuilder<T>, ScopedBindingBuilder {

    DefaultBindingBuilder(Key<T> key, List<AbstractBinding<?>> bindingList) {
        super(key, bindingList);
    }

    @Override
    public ScopedBindingBuilder auto() {
        AbstractBinding<T> base = getBinding();
        setBinding(new AutoBindingImpl<>(base.getKey(), base.getScoping()));
        return this;
    }

    @Override
    public ScopedBindingBuilder to(Class<? extends T> target) {
        to(Key.of(target));
        return this;
    }

    @Override
    public ScopedBindingBuilder to(Key<? extends T> target) {
        AbstractBinding<T> base = getBinding();
        setBinding(new LinkedKeyBindingImpl<>(base.getKey(), base.getScoping(), target));
        return this;
    }

    @Override
    public void toInstance(T instance) {
        AbstractBinding<T> base = getBinding();
        setBinding(new InstanceBindingImpl<>(base.getKey(), base.getScoping(), instance));
    }

    @Override
    public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor) {
        AbstractBinding<T> base = getBinding();
        setBinding(new ConstructorBindingImpl<>(base.getKey(), base.getScoping(), InjectionPoint.forConstructor(constructor)));
        return this;
    }

    @Override
    public ScopedBindingBuilder toProvider(Class<? extends Provider<? extends T>> provider) {
        toProvider(Key.of(provider));
        return this;
    }

    @Override
    public ScopedBindingBuilder toProvider(Key<? extends Provider<? extends T>> provider) {
        AbstractBinding<T> base = getBinding();
        setBinding(new ProviderKeyBindingImpl<>(base.getKey(), base.getScoping(), provider));
        return this;
    }

    @Override
    public ScopedBindingBuilder toProvider(Provider<? extends T> provider) {
        AbstractBinding<T> base = getBinding();
        setBinding(new ProviderBindingImpl<>(base.getKey(), base.getScoping(), provider));
        return this;
    }

    @Override
    public ScopedBindingBuilder toComposer(Composer composer) {
        AbstractBinding<T> base = getBinding();
        setBinding(new ComposerBindingImpl<>(base.getKey(), base.getScoping(), composer));
        return this;
    }

    @Override
    public ScopedBindingBuilder toComposer(Class<? extends Composer> composer) {
        toComposer(Key.of(composer));
        return this;
    }

    @Override
    public ScopedBindingBuilder toComposer(Key<? extends Composer> composer) {
        AbstractBinding<T> base = getBinding();
        setBinding(new ComposerKeyBindingImpl<>(base.getKey(), base.getScoping(), composer));
        return this;
    }

    @Override
    public void in(Class<? extends Annotation> scopeAnnotation) {
        setBinding(getBinding().withScoping(Scoping.forAnnotation(scopeAnnotation)));
    }

    @Override
    public void in(Scope scope) {
        setBinding(getBinding().withScoping(Scoping.forInstance(scope)));
    }
}
