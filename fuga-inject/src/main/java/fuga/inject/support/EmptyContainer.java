package fuga.inject.support;

import fuga.common.Key;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public class EmptyContainer implements Container {
    @Override
    public <T> AbstractBinding<T> getExplicitBinding(Key<T> key) {
        return null;
    }

    @Override
    public <T> List<AbstractBinding<T>> getAllExplicitBindings(Key<T> key) {
        return Collections.emptyList();
    }

    @Override
    public ScopeBinding getScopeBinding(Class<? extends Annotation> annotationType) {
        return null;
    }

    @Override
    public List<AbstractMatchedWatching> getMatchedWatchings(Key<?> key) {
        return Collections.emptyList();
    }

    @Override
    public <T> List<AbstractKeyedWatching<T>> getKeyedWatchings(Key<T> key) {
        return Collections.emptyList();
    }

    @Override
    public void putBinding(AbstractBinding<?> binding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putScopeBinding(ScopeBinding scopeBinding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void putKeyedWatching(AbstractKeyedWatching<T> watching) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putMatchedWatching(AbstractMatchedWatching listenerBinding) {
        throw new UnsupportedOperationException();
    }
}
