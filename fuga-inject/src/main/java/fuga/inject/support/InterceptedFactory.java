package fuga.inject.support;

import fuga.inject.Dependency;
import fuga.common.Key;

import java.util.List;

class InterceptedFactory<T> implements InternalFactory<T> {

    private final Key<T> key;
    private final InternalFactory<T> delegateFactory;
    private final List<TypedInterceptor<T>> typedInterceptors;
    private final List<Interceptor> interceptors;

    InterceptedFactory(Key<T> key, InternalFactory<T> delegateFactory, List<TypedInterceptor<T>> typedInterceptors, List<Interceptor> interceptors) {
        this.key = key;
        this.delegateFactory = delegateFactory;
        this.typedInterceptors = typedInterceptors;
        this.interceptors = interceptors;
    }

    @Override
    public T get(InjectorContext context, Dependency<?> dependency) throws InternalProvisionException {
        var instance = delegateFactory.get(context, dependency);

        try {
            typedInterceptors.forEach(i -> i.intercept(context, key, instance));
            interceptors.forEach(i -> i.intercept(context, key, instance));
        } catch (RuntimeException e) {
            throw InternalProvisionException.errorInProvisionListener(e);
        }

        return instance;
    }
}
