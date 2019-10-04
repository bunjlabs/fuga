package com.bunjlabs.fuga.inject;

import java.util.Objects;

public class Key<T> {

    private final Class<T> type;
    private final int hashCode;
    private String toString;

    private Key(Class<T> type) {
        this.type = type;
        this.hashCode = computeHashCode();
    }

    public static <T> Key<T> of(Class<T> type) {
        return new Key<>(type);
    }

    public Class<T> getType() {
        return type;
    }

    private int computeHashCode() {
        return Objects.hash(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key<?> key = (Key<?>) o;
        return type.equals(key.type);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        String s = toString;
        if (s == null) {
            s = Key.class.getSimpleName() + "[" + type + "]";
            toString = s;
        }
        return s;
    }
}
