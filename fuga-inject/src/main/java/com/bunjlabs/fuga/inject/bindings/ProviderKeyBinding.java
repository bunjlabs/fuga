package com.bunjlabs.fuga.inject.bindings;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;

public interface ProviderKeyBinding<T> extends Binding<T> {

    Key<? extends Provider<? extends T>> getProviderKey();
}
