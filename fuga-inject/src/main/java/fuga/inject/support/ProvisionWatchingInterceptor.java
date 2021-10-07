package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.ProvisionListener;

class ProvisionWatchingInterceptor implements Interceptor {
    private final ProvisionListener listener;

    ProvisionWatchingInterceptor(ProvisionListener listener) {
        this.listener = listener;
    }

    @Override
    public <T> void intercept(InjectorContext injectorContext, Key<T> key, T instance) {
        listener.onProvision(key, instance);
    }
}
