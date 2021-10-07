package fuga.inject.builder;

import fuga.common.Key;
import fuga.inject.TypeListener;

public interface TypeWatchingBuilder<T> {

    void with(TypeListener<T> listener);

    void with(Class<? extends TypeListener<T>> listenerClass);

    void with(Key<? extends TypeListener<T>> listenerKey);
}
