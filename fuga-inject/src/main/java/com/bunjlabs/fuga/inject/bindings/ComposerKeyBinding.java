package com.bunjlabs.fuga.inject.bindings;

import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;

public interface ComposerKeyBinding<T> extends Binding<T> {

    Key<? extends Composer> getComposerKey();
}
