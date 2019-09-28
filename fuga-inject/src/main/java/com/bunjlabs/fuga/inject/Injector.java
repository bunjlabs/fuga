package com.bunjlabs.fuga.inject;

public interface Injector {

    <T> Binding<T> getBinding(Class<T> type);

    <T> Binding<T> getBinding(Key<T> key);

    <T> Provider<T> getProvider(Class<T> type);

    <T> Provider<T> getProvider(Key<T> key);

    <T> T getInstance(Class<T> type);

    <T> T getInstance(Key<T> key);
}
