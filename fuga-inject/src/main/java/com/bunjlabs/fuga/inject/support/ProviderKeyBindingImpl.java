package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;
import com.bunjlabs.fuga.inject.bindings.ProviderKeyBinding;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

class ProviderKeyBindingImpl<T> extends AbstractBinding<T> implements ProviderKeyBinding<T> {

    private final Key<? extends Provider<? extends T>> providerKey;

    ProviderKeyBindingImpl(Key<T> key, Scoping scoping, Key<? extends Provider<? extends T>> providerKey) {
        super(key, scoping);
        this.providerKey = providerKey;
    }

    ProviderKeyBindingImpl(Key<T> key, Key<? extends Provider<? extends T>> providerKey, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.providerKey = providerKey;
    }

    @Override
    public Key<? extends Provider<? extends T>> getProviderKey() {
        return providerKey;
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new ProviderKeyBindingImpl<>(getKey(), scoping, providerKey);
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
        if (o instanceof ProviderKeyBinding) {
            ProviderKeyBinding<?> other = (ProviderKeyBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(providerKey, other.getProviderKey());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), providerKey);
    }
}
