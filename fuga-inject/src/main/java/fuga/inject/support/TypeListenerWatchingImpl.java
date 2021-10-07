package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.KeyedWatchingVisitor;
import fuga.inject.TypeListener;
import fuga.inject.watchings.TypeListenerWatching;

public class TypeListenerWatchingImpl<T> extends AbstractKeyedWatching<T> implements TypeListenerWatching<T> {
    private final TypeListener<T> listener;

    public TypeListenerWatchingImpl(Key<T> key, TypeListener<T> listener) {
        super(key);
        this.listener = listener;
    }

    public TypeListenerWatchingImpl(Key<T> key, TypeListener<T> listener, TypedInterceptor<T> interceptor) {
        super(key, interceptor);
        this.listener = listener;
    }

    @Override
    public TypeListener<T> getTypeListener() {
        return listener;
    }

    @Override
    public <V> V acceptVisitor(KeyedWatchingVisitor<T, V> visitor) {
        return visitor.visit(this);
    }

}
