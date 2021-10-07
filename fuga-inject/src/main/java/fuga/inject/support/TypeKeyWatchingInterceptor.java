package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.TypeListener;

class TypeKeyWatchingInterceptor<T> implements TypedInterceptor<T> {
    private final Key<? extends TypeListener<T>> listenerKey;

    TypeKeyWatchingInterceptor(Key<? extends TypeListener<T>> listenerKey) {
        this.listenerKey = listenerKey;
    }

    @Override
    public void intercept(InjectorContext injectorContext, Key<T> key, T instance) {
        var listener = injectorContext.getInjector().getInstance(listenerKey);
        listener.onProvision(key, instance);
    }
}
