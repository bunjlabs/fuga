package com.bunjlabs.fuga.inject.bindings;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Key;

public interface LinkedKeyBinding<T> extends Binding<T> {

    Key<? extends T> getLinkedKey();
}
