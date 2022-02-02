package fuga.inject.support;

import fuga.inject.Dependency;

class AttributeDependencyInjector<T> implements DependencyInjector<T> {
    private final Dependency<T> dependency;

    AttributeDependencyInjector(Dependency<T> dependency) {
        this.dependency = dependency;
    }

    @Override
    public T inject(InjectorContext context) throws InternalProvisionException {
        var attribute = context.getAttribute(dependency.getKey());

        if (attribute == null && !dependency.isNullable()) {
            throw InternalProvisionException.nullInjectedIntoNonNullableDependency(context.getRequester().getType(), dependency);
        }

        return attribute;
    }
}
