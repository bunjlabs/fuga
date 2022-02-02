package fuga.inject.builder;

import fuga.common.Key;
import fuga.inject.Encounter;
import fuga.inject.ProvisionListener;

public interface MatchBuilder<T> {

    default void attach(Class<?> attachmentType) {
        attach(Key.of(attachmentType));
    }

    void attach(Key<?> attachmentKey);

    default void configure(Class<? extends Encounter<T>> encounterType) {
        configure(Key.of(encounterType));
    }

    void configure(Key<? extends Encounter<T>> encounterKey);

    void configure(Encounter<T> encounter);

    default void watch(Class<? extends ProvisionListener<T>> listenerType) {
        watch(Key.of(listenerType));
    }

    void watch(Key<? extends ProvisionListener<T>> listenerKey);

    void watch(ProvisionListener<T> listener);

}
