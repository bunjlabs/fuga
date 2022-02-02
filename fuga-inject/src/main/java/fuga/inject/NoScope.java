package fuga.inject;

import fuga.common.Key;

public class NoScope implements Scope {

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return unscoped;
    }

    @Override
    public String toString() {
        return "Scopes.NO_SCOPE";
    }
}
