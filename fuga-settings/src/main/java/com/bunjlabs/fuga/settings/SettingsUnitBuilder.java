package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.inject.Unit;
import com.bunjlabs.fuga.inject.UnitBuilder;
import com.bunjlabs.fuga.environment.Environment;
import com.bunjlabs.fuga.settings.source.SettingsSource;
import com.bunjlabs.fuga.settings.support.DefaultSettingsComposer;
import com.bunjlabs.fuga.settings.support.DefaultSettingsContainer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SettingsUnitBuilder implements UnitBuilder {

    private Environment environment;
    private List<SettingsSource> settingsSourcesSet;
    private MutableSettingsNode settingsTree;

    public SettingsUnitBuilder() {
        this.environment = Environment.DEFAULT;
        this.settingsSourcesSet = new LinkedList<>();
    }

    public SettingsUnitBuilder withEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public SettingsUnitBuilder withSettingsTree(MutableSettingsNode settingsTree) {
        this.settingsTree = settingsTree;
        return this;
    }

    public SettingsUnitBuilder withSettingsSources(SettingsSource... settingsSources) {
        settingsSourcesSet.addAll(Arrays.asList(settingsSources));
        return this;
    }

    @Override
    public Unit build() {
        return c -> {
            var container = settingsTree != null
                    ? new DefaultSettingsContainer(settingsTree)
                    : new DefaultSettingsContainer();
            var composer = new DefaultSettingsComposer(container);

            settingsSourcesSet.forEach(source -> container.load(source, environment));

            c.bind(SettingsContainer.class).toInstance(container);
            c.bind(SettingsComposer.class).toInstance(composer);
        };
    }
}
