package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.Binding;
import fuga.inject.Encounter;

public class InitializableEncounter<T> implements Initializable {
    private final Binding<T> binding;
    private final Key<? extends Encounter<T>> encounterKey;

    public InitializableEncounter(Binding<T> binding, Key<? extends Encounter<T>> encounterKey) {
        this.binding = binding;
        this.encounterKey = encounterKey;
    }

    @Override
    public void initialize(InjectorImpl injector) {
        injector.getInstance(encounterKey).configure(binding);
    }
}
