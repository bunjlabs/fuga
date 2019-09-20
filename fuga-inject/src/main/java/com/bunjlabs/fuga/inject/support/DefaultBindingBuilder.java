package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;
import com.bunjlabs.fuga.inject.Scope;
import com.bunjlabs.fuga.inject.binder.BindingBuilder;
import com.bunjlabs.fuga.inject.binder.ScopedBindingBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

public class DefaultBindingBuilder<T> extends AbstractBindingBuilder<T> implements BindingBuilder<T>, ScopedBindingBuilder {

    DefaultBindingBuilder(Key<T> key, BindingProcessor bindingProcessor) {
        super(key, bindingProcessor);
    }

    @Override
    public void auto() {
        AbstractBinding<T> base = getBinding();
        setBinding(new AutoBinding<>(base.getKey()));
    }

    @Override
    public void to(Class<? extends T> target) {
        to(Key.of(target));
    }

    @Override
    public void to(Key<? extends T> target) {
        AbstractBinding<T> base = getBinding();
        setBinding(new LinkedKeyBinding<>(base.getKey(), target));
    }

    @Override
    public void toInstance(T instance) {
        AbstractBinding<T> base = getBinding();
        setBinding(new InstanceBinding<>(base.getKey(), instance));
    }

    @Override
    public void toConstructor(Constructor<T> constructor) {
        AbstractBinding<T> base = getBinding();
        setBinding(new ConstructorBinding<>(base.getKey(), InjectionPoint.forConstructor(constructor)));
    }

    @Override
    public void toProvider(Class<? extends Provider<? extends T>> provider) {
        toProvider(Key.of(provider));
    }

    @Override
    public void toProvider(Key<? extends Provider<? extends T>> provider) {
        AbstractBinding<T> base = getBinding();
        setBinding(new ProviderBinding<>(base.getKey(), provider));
    }

    @Override
    public void toProvider(Provider<? extends T> provider) {
        AbstractBinding<T> base = getBinding();
        setBinding(new ProviderInstanceBinding<>(base.getKey(), provider));
    }

    @Override
    public void toComposer(Composer composer) {
        AbstractBinding<T> base = getBinding();
        setBinding(new ComposerInstanceBinding<>(base.getKey(), composer));
    }

    @Override
    public void toComposer(Class<? extends Composer> composer) {
        toComposer(Key.of(composer));
    }

    @Override
    public void toComposer(Key<? extends Composer> composer) {
        AbstractBinding<T> base = getBinding();
        setBinding(new ComposerBinding<>(base.getKey(), composer));

    }

    @Override
    public void in(Class<? extends Annotation> scopeAnnotation) {
        //getBinding().setScope(scope);
    }

    @Override
    public void in(Scope scope) {
        getBinding().setScope(scope);
    }
}
