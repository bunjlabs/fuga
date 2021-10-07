package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.KeyedWatching;

abstract class AbstractKeyedWatching<T> implements KeyedWatching<T> {

    private final Key<T> key;
    private final TypedInterceptor<T> interceptor;

    AbstractKeyedWatching(Key<T> key) {
        this.key = key;
        this.interceptor = null;
    }

    AbstractKeyedWatching(Key<T> key, TypedInterceptor<T> interceptor) {
        this.key = key;
        this.interceptor = interceptor;
    }

    @Override
    public Key<T> getKey() {
        return key;
    }

    TypedInterceptor<T> getInterceptor() {
        return interceptor;
    }
}
