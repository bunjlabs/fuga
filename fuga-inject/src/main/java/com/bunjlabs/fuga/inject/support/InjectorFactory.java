package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Dependency;
import com.bunjlabs.fuga.inject.Injector;

class InjectorFactory implements InternalFactory<Injector> {
    @Override
    public Injector get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        return context.getInjector();
    }
}
