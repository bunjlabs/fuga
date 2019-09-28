package com.bunjlabs.fuga.inject.binder;

import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;

import java.lang.reflect.Constructor;

public interface LinkedBindingBuilder<T> extends ScopedBindingBuilder {

    ScopedBindingBuilder to(Class<? extends T> target);

    ScopedBindingBuilder to(Key<? extends T> target);

    void toInstance(T instance);

    <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor);

    ScopedBindingBuilder toProvider(Provider<? extends T> provider);

    ScopedBindingBuilder toProvider(Class<? extends Provider<? extends T>> provider);

    ScopedBindingBuilder toProvider(Key<? extends Provider<? extends T>> provider);

    ScopedBindingBuilder toComposer(Composer composer);

    ScopedBindingBuilder toComposer(Class<? extends Composer> composer);

    ScopedBindingBuilder toComposer(Key<? extends Composer> composer);
}
