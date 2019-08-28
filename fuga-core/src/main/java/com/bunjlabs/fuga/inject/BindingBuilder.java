package com.bunjlabs.fuga.inject;

public interface BindingBuilder<T> {

    void to(Class<? extends T> target);

    void to(Key<? extends T> target);

    void toInstance(T instance);

    void toProvider(Class<? extends Provider<? extends T>> provider);

    void toProvider(Key<? extends Provider<? extends T>> provider);

    void toProvider(Provider<? extends T> provider);
}
