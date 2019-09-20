package com.bunjlabs.fuga.inject.binder;

import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.Provider;

import java.lang.reflect.Constructor;

public interface LinkedBindingBuilder<T> extends ScopedBindingBuilder {

    void to(Class<? extends T> target);

    void to(Key<? extends T> target);

    void toInstance(T instance);

    void toConstructor(Constructor<T> constructor);

    void toProvider(Provider<? extends T> provider);

    void toProvider(Class<? extends Provider<? extends T>> provider);

    void toProvider(Key<? extends Provider<? extends T>> provider);

    void toComposer(Composer composer);

    void toComposer(Class<? extends Composer> composer);

    void toComposer(Key<? extends Composer> composer);
}
