package fuga.inject.support;

import fuga.inject.Dependency;
import fuga.lang.TypeLiteral;

public class SourceDependencyInjector<T> implements DependencyInjector<T> {
    private final Dependency<T> dependency;

    public SourceDependencyInjector(Dependency<T> dependency) {
        this.dependency = dependency;
    }

    @Override
    public T inject(InjectorContext context) throws InternalProvisionException {
        var instance = context.getSource();

        if (instance == null && !dependency.isNullable()) {
            throw InternalProvisionException.nullInjectedIntoNonNullableDependency(context.getRequester().getType(), dependency);
        }

        if (instance != null) {
            var instanceType = TypeLiteral.of(instance.getClass());
            if (!dependency.getType().isAssignableFrom(instanceType)) {
                throw InternalProvisionException.sourceDependencyHasUnexpectedType(dependency, instanceType);
            }
        }


        return doCast(instance);
    }

    @SuppressWarnings("unchecked")
    private T doCast(Object instance) {
        return (T) instance;
    }
}
