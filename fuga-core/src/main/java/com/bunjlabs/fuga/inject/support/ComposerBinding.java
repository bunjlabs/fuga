package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.BindingVisitor;
import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;

public class ComposerBinding<T> extends AbstractBinding<T> {

    private final Key<? extends Composer> composerKey;

    ComposerBinding(Key<T> key, Key<? extends Composer> composerKey) {
        super(key);
        this.composerKey = composerKey;
    }

    ComposerBinding(Key<T> key, Key<? extends Composer> composerKey, InternalFactory<T> internalFactory) {
        super(key, internalFactory);
        this.composerKey = composerKey;
    }

    public Key<? extends Composer> getComposerKey() {
        return composerKey;
    }

    @Override
    public <V> V acceptVisitor(BindingVisitor<? super T, V> visitor) {
        return visitor.visit(this);
    }
}
