package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

public class ComposerInstanceBinding<T> extends AbstractBinding<T> {
    private final Composer composer;

    ComposerInstanceBinding(Key<T> key, Composer composer) {
        super(key);
        this.composer = composer;
    }

    ComposerInstanceBinding(Key<T> key, Composer composer, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.composer = composer;
    }

    Composer getComposer() {
        return composer;
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("key", getKey())
                .add("composer", composer)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ComposerInstanceBinding) {
            ComposerInstanceBinding<?> other = (ComposerInstanceBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(composer, other.composer);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), composer);
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }
}
