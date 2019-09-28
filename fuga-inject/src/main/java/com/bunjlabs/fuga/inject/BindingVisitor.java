package com.bunjlabs.fuga.inject;

import com.bunjlabs.fuga.inject.bindings.*;

public interface BindingVisitor<T, V> {

    V visit(InstanceBinding<? extends T> binding);

    V visit(ConstructorBinding<? extends T> binding);

    V visit(ProviderKeyBinding<? extends T> binding);

    V visit(ProviderBinding<? extends T> binding);

    V visit(ComposerKeyBinding<? extends T> binding);

    V visit(ComposerBinding<? extends T> binding);

    V visit(LinkedKeyBinding<? extends T> binding);

    V visit(AutoBinding<? extends T> binding);

    V visit(UntargettedBinding<? extends T> binding);
}