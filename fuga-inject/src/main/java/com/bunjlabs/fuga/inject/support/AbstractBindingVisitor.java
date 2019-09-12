package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Key;

abstract class AbstractBindingVisitor<T, V> implements BindingVisitor<T, V> {
    final Key<T> key;

    AbstractBindingVisitor(Binding<T> binding) {
        key = binding.getKey();
    }

    protected V visitOther(Binding<? extends T> binding) {
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
    public V visit(ProviderBinding<? extends T> binding) {
        return visitOther(binding);
    }

    @Override
    public V visit(ProviderInstanceBinding<? extends T> binding) {
        return visitOther(binding);
    }

    @Override
    public V visit(ComposerBinding<? extends T> binding) {
        return visitOther(binding);
    }

    @Override
    public V visit(ComposerInstanceBinding<? extends T> binding) {
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
