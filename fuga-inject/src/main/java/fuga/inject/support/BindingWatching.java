package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.ProvisionListener;
import fuga.lang.TypeLiteral;
import fuga.util.Matcher;

class BindingWatching<T> extends BindingMatcher<T> {
    private final Key<? extends ProvisionListener<T>> listenerKey;
    private final ProvisionListener<T> listener;

    BindingWatching(Matcher<? super TypeLiteral<T>> matcher, Key<? extends ProvisionListener<T>> listenerKey, ProvisionListener<T> listener) {
        super(matcher);
        this.listenerKey = listenerKey;
        this.listener = listener;
    }

    Key<? extends ProvisionListener<T>> getListenerKey() {
        return listenerKey;
    }

    ProvisionListener<T> getListener() {
        return listener;
    }
}
