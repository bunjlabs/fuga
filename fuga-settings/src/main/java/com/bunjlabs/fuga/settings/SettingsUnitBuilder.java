package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.inject.Unit;
import com.bunjlabs.fuga.inject.UnitBuilder;
import com.bunjlabs.fuga.settings.environment.DefaultEnvironment;
import com.bunjlabs.fuga.settings.environment.Environment;
import com.bunjlabs.fuga.settings.source.SettingsSource;
import com.bunjlabs.fuga.settings.support.DefaultSettingsComposer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SettingsUnitBuilder implements UnitBuilder {

    private Environment environment;
    private Set<SettingsSource> settingsSourcesSet;
    private Set<Class<?>> interfacesSet;

    public SettingsUnitBuilder() {
        this.environment = new DefaultEnvironment();
        this.settingsSourcesSet = new HashSet<>();
        this.interfacesSet = new HashSet<>();
    }

    public SettingsUnitBuilder withEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public SettingsUnitBuilder withSettingsSources(SettingsSource... settingsSources) {
        settingsSourcesSet.addAll(Arrays.asList(settingsSources));
        return this;
    }

    @Override
    public Unit build() {
        return configuration -> {
            DefaultSettingsComposer settingsFactory = new DefaultSettingsComposer();

            settingsSourcesSet.forEach(source -> settingsFactory.loadFromSettingsSource(source, environment));

            configuration.bind(SettingsComposer.class).toInstance(settingsFactory);
        };
    }
}
