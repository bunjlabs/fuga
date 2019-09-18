package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.environment.Environment;
import com.bunjlabs.fuga.settings.source.SettingsSource;

public interface SettingsContainer {

    void checkAvailability() throws SecurityException;

    void merge(SettingsNode settingsNode);

    void load(SettingsSource settingsSource, Environment environment);

    void persist(MutableSettingsNode settingTree);
}
