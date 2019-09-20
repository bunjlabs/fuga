package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;
import com.bunjlabs.fuga.inject.Scope;

public class SingletonScope implements Scope {
    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
        return new Provider<>() {
            volatile T instance;

            @Override
            public T get() {
                if (instance == null) {
                    instance = provider.get();
                }
                return instance;
            }
        };
    }
}
