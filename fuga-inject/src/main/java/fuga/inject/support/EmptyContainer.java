package fuga.inject.support;

import fuga.common.Key;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

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
    public <T> List<BindingAttachment<T>> getAttachments(Key<T> key) {
        return Collections.emptyList();
    }

    @Override
    public <T> List<BindingEncounter<T>> getEncounters(Key<T> key) {
        return Collections.emptyList();
    }

    @Override
    public <T> List<BindingWatching<T>> getWatchings(Key<T> key) {
        return Collections.emptyList();
    }

    @Override
    public void putBinding(AbstractBinding<?> binding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAttachment(BindingAttachment<?> attachment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putEncounter(BindingEncounter<?> encounter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putWatching(BindingWatching<?> watching) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<? extends AbstractBinding<?>> action) {
    }
}
