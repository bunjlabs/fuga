package fuga.inject.watchings;

import fuga.inject.MatchedWatching;
import fuga.inject.ProvisionListener;

public interface ProvisionListenerWatching extends MatchedWatching {

    ProvisionListener getProvisionListener();
}
