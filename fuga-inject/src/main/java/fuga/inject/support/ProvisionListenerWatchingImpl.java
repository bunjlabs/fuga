package fuga.inject.support;

import fuga.inject.MatchedWatchingVisitor;
import fuga.inject.ProvisionListener;
import fuga.inject.watchings.ProvisionListenerWatching;
import fuga.lang.FullType;
import fuga.util.Matcher;

public class ProvisionListenerWatchingImpl extends AbstractMatchedWatching implements ProvisionListenerWatching {
    private final ProvisionListener listener;

    public ProvisionListenerWatchingImpl(Matcher<FullType<?>> matcher, ProvisionListener listener) {
        super(matcher);
        this.listener = listener;
    }

    public ProvisionListenerWatchingImpl(Matcher<FullType<?>> matcher, ProvisionListener listener, Interceptor interceptor) {
        super(matcher, interceptor);
        this.listener = listener;
    }

    @Override
    public ProvisionListener getProvisionListener() {
        return listener;
    }

    @Override
    public <V> V acceptVisitor(MatchedWatchingVisitor<V> visitor) {
        return visitor.visit(this);
    }

}
