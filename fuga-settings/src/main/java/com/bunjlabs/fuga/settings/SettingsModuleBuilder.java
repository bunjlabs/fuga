package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.ioc.Module;
import com.bunjlabs.fuga.ioc.ModuleBuilder;
import com.bunjlabs.fuga.settings.environment.DefaultEnvironment;
import com.bunjlabs.fuga.settings.environment.Environment;
import com.bunjlabs.fuga.settings.source.EmptySettingsSource;
import com.bunjlabs.fuga.settings.source.SettingsSource;
import com.bunjlabs.fuga.settings.support.DefaultSettingsFactory;
import com.bunjlabs.fuga.util.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SettingsModuleBuilder implements ModuleBuilder {

    private SettingsSource settingsSource;
    private Environment environment;
    private Set<Class<?>> interfacesSet;

    public SettingsModuleBuilder() {
        this.settingsSource = new EmptySettingsSource();
        this.environment = new DefaultEnvironment();
        this.interfacesSet = new HashSet<>();
    }

    public SettingsModuleBuilder withSettingsSource(SettingsSource settingsSource) {
        this.settingsSource = settingsSource;
        return this;
    }

    public SettingsModuleBuilder withEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public SettingsModuleBuilder withInterfaces(Class<?>... interfaces) {
        for (Class<?> i : interfaces) Assert.isTrue(i.isInterface(), "all input classes must be an interfaces");
        interfacesSet.addAll(Arrays.asList(interfaces));
        return this;
    }

    @Override
    public Module build() {
        return configuration -> {
            DefaultSettingsFactory settingsFactory = new DefaultSettingsFactory();

            interfacesSet.forEach(i -> configuration.add(i, settingsFactory.provide(i)));
            settingsFactory.loadFromSettingsSource(settingsSource, environment);
            if (!settingsFactory.checkAvailibility()) {
                throw new SettingsException("Not all settings contain value.");
            }
        };
    }
}
