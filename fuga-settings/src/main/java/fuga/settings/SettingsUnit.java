package fuga.settings;

import fuga.environment.Environment;
import fuga.inject.Configuration;
import fuga.inject.Inject;
import fuga.inject.Singleton;
import fuga.inject.Unit;
import fuga.settings.source.SettingsSource;
import fuga.settings.support.DefaultSettingsComposer;
import fuga.settings.support.DefaultSettingsConfigurator;
import fuga.settings.support.DefaultSettingsContainer;
import fuga.settings.support.DefaultSettingsNode;
import fuga.util.Matchers;

import java.util.Collections;
import java.util.List;

public class SettingsUnit implements Unit {

    private final Environment environment;
    private final MutableSettingsNode settingsTree;
    private final List<SettingsSource> settingsSources;

    @Inject
    public SettingsUnit() {
        this.environment = Environment.DEFAULT;
        this.settingsTree = new DefaultSettingsNode();
        this.settingsSources = Collections.emptyList();
    }

    public SettingsUnit(Environment environment, MutableSettingsNode settingsTree, List<SettingsSource> settingsSources) {
        this.environment = environment;
        this.settingsTree = settingsTree;
        this.settingsSources = settingsSources;
    }

    @Override
    public void setup(Configuration c) {
        c.bind(SettingsContainer.class)
                .toProvider(() -> new DefaultSettingsContainer(settingsTree))
                .in(Singleton.class);
        c.match(SettingsContainer.class)
                .watch(container -> settingsSources.forEach(source -> container.load(source, environment)));

        c.bind(DefaultSettingsConfigurator.class).in(Singleton.class);
        c.bind(SettingsConfigurator.class).to(DefaultSettingsConfigurator.class);
        c.match(Matchers.has(Settings.class)).configure(SettingsConfigurator.class);

        c.bind(DefaultSettingsComposer.class).in(Singleton.class);
        c.bind(SettingsComposer.class).to(DefaultSettingsComposer.class);
    }
}
