package fuga.inject.watchings;

import fuga.inject.KeyedWatching;
import fuga.inject.TypeListener;

public interface TypeListenerWatching<T> extends KeyedWatching<T> {

    TypeListener<T> getTypeListener();
}
