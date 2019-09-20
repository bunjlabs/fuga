package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.util.ObjectUtils;

public class InitializableFactory<T> implements InternalFactory<T> {

    private final Initializable<T> initializable;

    InitializableFactory(Initializable<T> initializable) {
        this.initializable = initializable;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        T instance = initializable.get();

        if (instance == null) {
            throw new InternalProvisionException("Initializable returned null");
        }

        return initializable.get();
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(this)
                .add("initializable", initializable)
                .toString();
    }
}
