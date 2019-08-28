package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.util.ObjectUtils;

public class InitializableFactory<T> implements InternalFactory<T> {

    private final Initializable<T> initializable;

    InitializableFactory(Initializable<T> initializable) {
        this.initializable = initializable;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) {
        return initializable.get();
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("value", initializable)
                .toString();
    }
}
