package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.bindings.UntargettedBinding;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Objects;

class UntargettedBindingImpl<T> extends AbstractBinding<T> implements UntargettedBinding<T> {

    UntargettedBindingImpl(Key<T> key, Scoping scoping) {
        super(key, scoping);
    }

    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }

    @Override
    protected AbstractBinding<T> withScoping(Scoping scoping) {
        return new UntargettedBindingImpl<>(getKey(), scoping);
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("key", getKey())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UntargettedBinding) {
            UntargettedBinding<?> other = (UntargettedBinding<?>) o;
            return getKey().equals(other.getKey());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

}
