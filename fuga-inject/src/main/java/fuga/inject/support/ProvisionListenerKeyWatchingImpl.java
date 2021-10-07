package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.MatchedWatchingVisitor;
import fuga.inject.ProvisionListener;
import fuga.inject.watchings.ProvisionListenerKeyWatching;
import fuga.lang.FullType;
import fuga.util.Matcher;

public class ProvisionListenerKeyWatchingImpl extends AbstractMatchedWatching implements ProvisionListenerKeyWatching {
    private final Key<? extends ProvisionListener> listenerKey;

    public ProvisionListenerKeyWatchingImpl(Matcher<FullType<?>> matcher, Key<? extends ProvisionListener> listenerKey) {
        super(matcher);
        this.listenerKey = listenerKey;
    }

    public ProvisionListenerKeyWatchingImpl(Matcher<FullType<?>> matcher, Key<? extends ProvisionListener> listenerKey, Interceptor interceptor) {
        super(matcher, interceptor);
        this.listenerKey = listenerKey;
    }

    @Override
    public Key<? extends ProvisionListener> getProvisionListenerKey() {
        return listenerKey;
    }

    @Override
    public <V> V acceptVisitor(MatchedWatchingVisitor<V> visitor) {
        return visitor.visit(this);
    }

}
