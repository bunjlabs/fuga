package fuga.inject.support;

import fuga.common.Key;

interface TypedInterceptor<T> {

    void intercept(InjectorContext injectorContext, Key<T> key, T instance);

}
