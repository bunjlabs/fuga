package com.bunjlabs.fuga.inject;

import java.lang.reflect.Constructor;

public interface BindingBuilder<T> {

    void auto();

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
