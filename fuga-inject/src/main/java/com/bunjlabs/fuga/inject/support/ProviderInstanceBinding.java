package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

public class ProviderInstanceBinding<T> extends AbstractBinding<T> {

    private final Provider<? extends T> provider;

    ProviderInstanceBinding(Key<T> key, Provider<? extends T> provider) {
        super(key);
        this.provider = provider;
    }

    public ProviderInstanceBinding(Key<T> key, Provider<? extends T> provider, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.provider = provider;
    }

    Provider<? extends T> getProvider() {
        return provider;
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
        if (o instanceof ProviderInstanceBinding) {
            ProviderInstanceBinding<?> other = (ProviderInstanceBinding<?>) o;
            return getKey().equals(other.getKey())
                    && Objects.equals(provider, other.provider);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), provider);
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }
}
