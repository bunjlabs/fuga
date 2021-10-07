package fuga.inject.watchings;

import fuga.common.Key;
import fuga.inject.MatchedWatching;
import fuga.inject.ProvisionListener;

public interface ProvisionListenerKeyWatching extends MatchedWatching {

    Key<? extends ProvisionListener> getProvisionListenerKey();
}
