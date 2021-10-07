package fuga.inject;

import fuga.common.Key;

public interface TypeListener<T> {

    void onProvision(Key<T> key, T instance);
}
