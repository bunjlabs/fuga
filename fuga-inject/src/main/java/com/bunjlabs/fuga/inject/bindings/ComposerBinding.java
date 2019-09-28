package com.bunjlabs.fuga.inject.bindings;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Composer;

public interface ComposerBinding<T> extends Binding<T> {

    Composer getComposer();
}
