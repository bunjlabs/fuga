package com.bunjlabs.fuga.settings.settings;

public interface MutableSettingsNode extends SettingsNode {

    MutableSettingsNode node(String name);

    void set(String key, SettingsValue value);

    void setAll(SettingsNode settingsNode);

    void merge(SettingsNode settingsNode);
}
