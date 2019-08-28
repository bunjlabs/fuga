package com.bunjlabs.fuga.inject;

public interface Binder {

    <T> BindingBuilder<T> bind(Class<T> type);

    <T> BindingBuilder<T> bind(Key<T> type);

}
