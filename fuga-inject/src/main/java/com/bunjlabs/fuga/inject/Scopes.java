package com.bunjlabs.fuga.inject;

import com.bunjlabs.fuga.inject.support.SingletonScope;

public abstract class Scopes {

    public static final Scope SINGLETON = new SingletonScope();

    public static final Scope NO_SCOPE =
            new Scope() {
                @Override
                public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
                    return unscoped;
                }

                @Override
                public String toString() {
                    return "Scopes.NO_SCOPE";
                }
            };
}
