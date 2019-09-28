package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;
import com.bunjlabs.fuga.inject.bindings.ProviderBinding;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

public class ProviderBindingImpl<T> extends AbstractBinding<T> implements ProviderBinding<T> {

    private final Provider<? extends T> provider;

    ProviderBindingImpl(Key<T> key, Scoping scoping, Provider<? extends T> provider) {
        super(key, scoping);
        this.provider = provider;
    }

    ProviderBindingImpl(Key<T> key, Provider<? extends T> provider, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.provider = provider;
    }

    @Override
    public Provider<? extends T> getProvider() {
        return provider;
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new ProviderBindingImpl<>(getKey(), scoping, provider);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("key", getKey())
                .add("provider", provider)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ProviderBinding) {
            ProviderBinding<?> other = (ProviderBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(provider, other.getProvider());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), provider);
    }
}
