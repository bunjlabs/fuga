package com.bunjlabs.fuga.inject.bindings;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Provider;

public interface ProviderBinding<T> extends Binding<T> {

    Provider<? extends T> getProvider();
}
