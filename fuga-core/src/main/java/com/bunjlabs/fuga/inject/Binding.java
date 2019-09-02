package com.bunjlabs.fuga.inject;

public interface Binding<T> {

    Key<T> getKey();

    <V> V acceptVisitor(BindingVisitor<? super T, V> visitor);
}
