package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.bindings.ComposerBinding;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

class ComposerBindingImpl<T> extends AbstractBinding<T> implements ComposerBinding<T> {

    private final Composer composer;

    ComposerBindingImpl(Key<T> key, Scoping scoping, Composer composer) {
        super(key, scoping);
        this.composer = composer;
    }

    ComposerBindingImpl(Key<T> key, Composer composer, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.composer = composer;
    }

    @Override
    public Composer getComposer() {
        return composer;
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new ComposerBindingImpl<>(getKey(), scoping, composer);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(ComposerBinding.class)
                .add("key", getKey())
                .add("composer", composer)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ComposerBinding) {
            ComposerBinding<?> other = (ComposerBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(composer, other.getComposer());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), composer);
    }
}
