package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Dependency;

interface InternalFactory<T> {

    T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException;
}
