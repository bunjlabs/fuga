package fuga.inject;

import fuga.common.Key;

public interface ProvisionListener {

    <T> void onProvision(Key<T> key, T instance);
}
