package fuga.inject.support;

import fuga.common.Key;

interface Interceptor {

    <T> void intercept(InjectorContext injectorContext, Key<T> key, T instance);

}
