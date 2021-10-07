package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.TypeListener;
import fuga.inject.builder.KeyedWatchingBuilder;

import java.util.List;

public class DefaultKeyedWatchingBuilder<T> implements KeyedWatchingBuilder<T> {

    private final Key<T> key;
    private final List<AbstractKeyedWatching<?>> watchings;

    public DefaultKeyedWatchingBuilder(Key<T> key, List<AbstractKeyedWatching<?>> watchings) {
        this.key = key;
        this.watchings = watchings;
    }

    @Override
    public void with(TypeListener<T> listener) {
        watchings.add(new TypeListenerWatchingImpl<>(key, listener));
    }

    @Override
    public void with(Class<? extends TypeListener<T>> listener) {
        with(Key.of(listener));
    }

    @Override
    public void with(Key<? extends TypeListener<T>> listenerKey) {
        watchings.add(new TypeListenerKeyWatchingImpl<>(key, listenerKey));
    }
}
