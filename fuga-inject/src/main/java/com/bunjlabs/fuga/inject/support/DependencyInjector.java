package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Dependency;

class DependencyInjector<T> {

    private final Dependency<T> dependency;
    private final InternalFactory<T> internalFactory;

    public DependencyInjector(Dependency<T> dependency, InternalFactory<T> internalFactory) {
        this.dependency = dependency;
        this.internalFactory = internalFactory;
    }

    T inject(InjectorContext context) throws InternalProvisionException {
        context.pushDependency(dependency);
        try {
            return internalFactory.get(context, dependency);
        } finally {
            context.popDependency();
        }
    }
}
