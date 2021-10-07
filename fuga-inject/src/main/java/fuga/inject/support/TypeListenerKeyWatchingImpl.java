package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.KeyedWatchingVisitor;
import fuga.inject.TypeListener;
import fuga.inject.watchings.TypeListenerKeyWatching;

public class TypeListenerKeyWatchingImpl<T> extends AbstractKeyedWatching<T> implements TypeListenerKeyWatching<T> {
    private final Key<? extends TypeListener<T>> listenerKey;

    public TypeListenerKeyWatchingImpl(Key<T> key, Key<? extends TypeListener<T>> listenerKey) {
        super(key);
        this.listenerKey = listenerKey;
    }

    public TypeListenerKeyWatchingImpl(Key<T> key, Key<? extends TypeListener<T>> listenerKey, TypedInterceptor<T> interceptor) {
        super(key, interceptor);
        this.listenerKey = listenerKey;
    }

    @Override
    public Key<? extends TypeListener<T>> getTypeListenerKey() {
        return listenerKey;
    }

    @Override
    public <V> V acceptVisitor(KeyedWatchingVisitor<T, V> visitor) {
        return visitor.visit(this);
    }
}
