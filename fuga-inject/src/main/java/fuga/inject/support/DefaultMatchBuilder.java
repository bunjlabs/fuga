package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.Encounter;
import fuga.inject.ProvisionListener;
import fuga.inject.builder.MatchBuilder;
import fuga.lang.TypeLiteral;
import fuga.util.Matcher;

import java.util.List;

public class DefaultMatchBuilder<T> implements MatchBuilder<T> {

    private final Matcher<? super TypeLiteral<T>> matcher;
    private final List<BindingAttachment<?>> attachments;
    private final List<BindingEncounter<?>> encounters;
    private final List<BindingWatching<?>> watchings;

    public DefaultMatchBuilder(Matcher<? super TypeLiteral<T>> matcher, List<BindingAttachment<?>> attachments, List<BindingEncounter<?>> encounters, List<BindingWatching<?>> watchings) {
        this.matcher = matcher;
        this.attachments = attachments;
        this.encounters = encounters;
        this.watchings = watchings;
    }

    @Override
    public void attach(Key<?> attachmentKey) {
        attachments.add(new BindingAttachment<>(matcher, attachmentKey));
    }

    @Override
    public void configure(Key<? extends Encounter<T>> encounterKey) {
        encounters.add(new BindingEncounter<>(matcher, encounterKey, null));
    }

    @Override
    public void configure(Encounter<T> encounter) {
        encounters.add(new BindingEncounter<>(matcher, null, encounter));
    }

    @Override
    public void watch(Key<? extends ProvisionListener<T>> listenerKey) {
        watchings.add(new BindingWatching<>(matcher, listenerKey, null));
    }

    @Override
    public void watch(ProvisionListener<T> listener) {
        watchings.add(new BindingWatching<>(matcher, null, listener));
    }
}
