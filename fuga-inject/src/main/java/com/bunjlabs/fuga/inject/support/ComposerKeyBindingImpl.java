package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.bindings.ComposerKeyBinding;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

class ComposerKeyBindingImpl<T> extends AbstractBinding<T> implements ComposerKeyBinding<T> {

    private final Key<? extends Composer> composerKey;

    ComposerKeyBindingImpl(Key<T> key, Scoping scoping, Key<? extends Composer> composerKey) {
        super(key, scoping);
        this.composerKey = composerKey;
    }

    ComposerKeyBindingImpl(Key<T> key, Key<? extends Composer> composerKey, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.composerKey = composerKey;
    }

    @Override
    public Key<? extends Composer> getComposerKey() {
        return composerKey;
    }

    @Override
    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new ComposerKeyBindingImpl<>(getKey(), scoping, composerKey);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(ComposerKeyBinding.class)
                .add("key", getKey())
                .add("composerKey", composerKey)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ComposerKeyBinding) {
            ComposerKeyBinding<?> other = (ComposerKeyBinding<?>) o;
            return getKey().equals(other.getKey())
                    && composerKey.equals(other.getComposerKey());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), composerKey);
    }
}
