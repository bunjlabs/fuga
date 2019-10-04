package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.bindings.LinkedKeyBinding;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

class LinkedKeyBindingImpl<T> extends AbstractBinding<T> implements LinkedKeyBinding<T> {

    private final Key<? extends T> linkedKey;

    LinkedKeyBindingImpl(Key<T> key, Scoping scoping, Key<? extends T> linkedKey) {
        super(key, scoping);
        this.linkedKey = linkedKey;
    }

    LinkedKeyBindingImpl(Key<T> key, Key<? extends T> linkedKey, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.linkedKey = linkedKey;
    }

    @Override
    public Key<? extends T> getLinkedKey() {
        return linkedKey;
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new LinkedKeyBindingImpl<>(getKey(), scoping, linkedKey);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(LinkedKeyBinding.class)
                .add("key", getKey())
                .add("linkedKey", linkedKey)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LinkedKeyBinding) {
            LinkedKeyBinding<?> other = (LinkedKeyBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(linkedKey, other.getLinkedKey());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), linkedKey);
    }
}
