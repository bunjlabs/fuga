package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.ProvisionListener;
import fuga.inject.builder.MatchedWatchingBuilder;
import fuga.lang.FullType;
import fuga.util.Matcher;

import java.util.List;

public class DefaultMatchedWatchingBuilder implements MatchedWatchingBuilder {
    private final Matcher<FullType<?>> matcher;
    private final List<AbstractMatchedWatching> watchings;

    public DefaultMatchedWatchingBuilder(Matcher<FullType<?>> matcher, List<AbstractMatchedWatching> watchings) {
        this.matcher = matcher;
        this.watchings = watchings;
    }

    @Override
    public void with(ProvisionListener listener) {
        watchings.add(new ProvisionListenerWatchingImpl(matcher, listener));
    }

    @Override
    public void with(Class<? extends ProvisionListener> listener) {
        with(Key.of(listener));
    }

    @Override
    public void with(Key<? extends ProvisionListener> listenerKey) {
        watchings.add(new ProvisionListenerKeyWatchingImpl(matcher, listenerKey));
    }
}
