package com.bunjlabs.fuga.inject.support;

public interface InternalFactory<T> {

    T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException;
}
