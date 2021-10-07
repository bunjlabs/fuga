package fuga.inject.builder;

import fuga.common.Key;
import fuga.inject.ProvisionListener;

public interface ProvisionWatchingBuilder {

    void with(ProvisionListener listener);

    void with(Class<? extends ProvisionListener> listenerClass);

    void with(Key<? extends ProvisionListener> listenerKey);
}
