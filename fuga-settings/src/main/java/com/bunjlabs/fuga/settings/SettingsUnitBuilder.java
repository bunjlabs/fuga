package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.ioc.Unit;
import com.bunjlabs.fuga.ioc.UnitBuilder;
import com.bunjlabs.fuga.settings.environment.DefaultEnvironment;
import com.bunjlabs.fuga.settings.environment.Environment;
import com.bunjlabs.fuga.settings.source.SettingsSource;
import com.bunjlabs.fuga.settings.support.DefaultSettingsFactory;
import com.bunjlabs.fuga.util.Assert;

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
    
    public SettingsUnitBuilder withInterfaces(Class<?>... interfaces) {
        for (Class<?> i : interfaces) Assert.isTrue(i.isInterface(), "All input classes must be an interface");

        interfacesSet.addAll(Arrays.asList(interfaces));
        return this;
    }

    @Override
    public Unit build() {
        return configuration -> {
            DefaultSettingsFactory settingsFactory = new DefaultSettingsFactory();

            interfacesSet.forEach(i -> configuration.add(i, settingsFactory.provide(i)));
            settingsSourcesSet.forEach(source -> settingsFactory.loadFromSettingsSource(source, environment));

            if (!settingsFactory.checkAvailibility()) {
                throw new SettingsException("Not all settings contains a value.");
            }
        };
    }
}
