package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

public class LinkBinding<T> extends AbstractBinding<T> {

    private final Key<? extends T> linkKey;

    LinkBinding(Key<T> key, Key<? extends T> implementationKey) {
        super(key);
        this.linkKey = implementationKey;
    }

    Key<? extends T> getLinkKey() {
        return linkKey;
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("key", getKey())
                .add("implementation", linkKey)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LinkBinding) {
            LinkBinding<?> other = (LinkBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(linkKey, other.linkKey);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), linkKey);
    }

    @Override
    public InternalFactory<T> getInternalFactory() {
        return new DelegatedFactory<>(linkKey);
    }
}
