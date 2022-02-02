package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.Binding;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract class AbstractBinding<T> implements Binding<T> {

    private final Map<Key<?>, Object> attributes = new ConcurrentHashMap<>();
    private final Key<T> key;

    AbstractBinding(Key<T> key) {
        this.key = key;
    }

    @Override
    public <A> A getAttribute(Key<A> attributeType) {
        return doGetAttribute(attributeType);
    }

    @Override
    public <A> void setAttribute(Key<A> attributeType, A value) {
        attributes.putIfAbsent(attributeType, value);
    }

    @SuppressWarnings("unchecked")
    private <A> A doGetAttribute(Key<A> attributeType) {
        return (A) attributes.get(attributeType);
    }

    @Override
    public Key<T> getKey() {
        return key;
    }

    @SuppressWarnings("unchecked")
    InternalFactory<T> getInternalFactory() {
        return getAttribute(Key.of(InternalFactory.class));
    }

    Scoping getScoping() {
        return getAttribute(Scoping.class);
    }

}
