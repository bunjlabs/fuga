package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

public class ProviderBinding<T> extends AbstractBinding<T> {

    private final Key<? extends Provider<? extends T>> providerKey;

    ProviderBinding(Key<T> key, Key<? extends Provider<? extends T>> providerKey) {
        super(key);
        this.providerKey = providerKey;
    }

    ProviderBinding(Key<T> key, Key<? extends Provider<? extends T>> providerKey, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.providerKey = providerKey;
    }

    Key<? extends Provider<? extends T>> getProviderKey() {
        return providerKey;
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("key", getKey())
                .add("providerKey", providerKey)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ProviderBinding) {
            ProviderBinding<?> other = (ProviderBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(providerKey, other.providerKey);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), providerKey);
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }
}
