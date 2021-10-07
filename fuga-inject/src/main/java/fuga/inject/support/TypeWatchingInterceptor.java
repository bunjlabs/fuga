package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.TypeListener;

class TypeWatchingInterceptor<T> implements TypedInterceptor<T> {
    private final TypeListener<T> listener;

    TypeWatchingInterceptor(TypeListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public void intercept(InjectorContext injectorContext, Key<T> key, T instance) {
        listener.onProvision(key, instance);
    }
}
