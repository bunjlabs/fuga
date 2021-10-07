package fuga.inject;

import fuga.common.Key;

public interface KeyedWatching<T> {

    Key<T> getKey();

    <V> V acceptVisitor(KeyedWatchingVisitor<T, V> visitor);

}
