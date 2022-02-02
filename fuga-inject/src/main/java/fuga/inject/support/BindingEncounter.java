package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.Encounter;
import fuga.lang.TypeLiteral;
import fuga.util.Matcher;

class BindingEncounter<T> extends BindingMatcher<T> {
    private final Key<? extends Encounter<T>> encounterKey;
    private final Encounter<T> encounter;

    BindingEncounter(Matcher<? super TypeLiteral<T>> matcher, Key<? extends Encounter<T>> encounterKey, Encounter<T> encounter) {
        super(matcher);
        this.encounterKey = encounterKey;
        this.encounter = encounter;
    }

    Key<? extends Encounter<T>> getEncounterKey() {
        return encounterKey;
    }

    Encounter<T> getEncounter() {
        return encounter;
    }
}
