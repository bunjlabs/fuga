package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

public class LinkedKeyBinding<T> extends AbstractBinding<T> {

    private final Key<? extends T> linkedKey;

    LinkedKeyBinding(Key<T> key, Key<? extends T> linkedKey) {
        super(key);
        this.linkedKey = linkedKey;
    }

    LinkedKeyBinding(Key<T> key, Key<? extends T> linkedKey, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.linkedKey = linkedKey;
    }

    Key<? extends T> getLinkedKey() {
        return linkedKey;
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("key", getKey())
                .add("linkedKey", linkedKey)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LinkedKeyBinding) {
            LinkedKeyBinding<?> other = (LinkedKeyBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(linkedKey, other.linkedKey);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), linkedKey);
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }
}
