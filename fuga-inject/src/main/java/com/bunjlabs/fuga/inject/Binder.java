package com.bunjlabs.fuga.inject;

import com.bunjlabs.fuga.inject.binder.BindingBuilder;

public interface Binder {

    <T> BindingBuilder<T> bind(Class<T> type);

    <T> BindingBuilder<T> bind(Key<T> type);

}
