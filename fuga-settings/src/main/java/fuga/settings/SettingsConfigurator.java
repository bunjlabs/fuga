package fuga.settings;

import fuga.inject.Binding;
import fuga.inject.ConfigurationException;
import fuga.inject.Encounter;

public interface SettingsConfigurator extends Encounter<Object> {

    @Override
    void configure(Binding<Object> binding) throws ConfigurationException;
}
