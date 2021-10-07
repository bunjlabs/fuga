package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.ProvisionListener;

class ProvisionKeyWatchingInterceptor implements Interceptor {
    private final Key<? extends ProvisionListener> listenerKey;

    ProvisionKeyWatchingInterceptor(Key<? extends ProvisionListener> listenerKey) {
        this.listenerKey = listenerKey;
    }

    @Override
    public <T> void intercept(InjectorContext injectorContext, Key<T> key, T instance) {
        var listener = injectorContext.getInjector().getInstance(listenerKey);
        listener.onProvision(key, instance);
    }
}
