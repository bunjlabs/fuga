package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;

import java.util.List;

public class DefaultBindingBuilder<T> extends AbstractBindingBuilder<T> {

    DefaultBindingBuilder(Key<T> key, List<Binding<?>> bindings) {
        super(key, bindings);
    }


    @Override
    public void to(Class<? extends T> target) {
        to(Key.of(target));
    }

    @Override
    public void to(Key<? extends T> target) {
        AbstractBinding<T> base = getBinding();
        setBinding(new LinkBinding<>(base.getKey(), target));
    }

    @Override
    public void toInstance(T instance) {
        AbstractBinding<T> base = getBinding();
        setBinding(new InstanceBinding<>(base.getKey(), instance));
    }

    @Override
    public void toProvider(Class<? extends Provider<? extends T>> provider) {
    }

    @Override
    public void toProvider(Key<? extends Provider<? extends T>> provider) {
    }

    @Override
    public void toProvider(Provider<? extends T> provider) {
        AbstractBinding<T> base = getBinding();
        setBinding(new ProviderInstanceBinding<>(base.getKey(), provider));
    }
}
