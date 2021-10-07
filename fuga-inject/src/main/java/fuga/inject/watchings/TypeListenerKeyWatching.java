package fuga.inject.watchings;

import fuga.common.Key;
import fuga.inject.KeyedWatching;
import fuga.inject.TypeListener;

public interface TypeListenerKeyWatching<T> extends KeyedWatching<T> {

    Key<? extends TypeListener<T>> getTypeListenerKey();
}
