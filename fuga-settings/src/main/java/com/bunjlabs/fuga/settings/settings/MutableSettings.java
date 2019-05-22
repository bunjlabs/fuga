package com.bunjlabs.fuga.settings.settings;

public interface MutableSettings extends Settings {

    MutableSettings node(String name);

    void set(String key, SettingsValue value);

    void setAll(Settings settings);

    void merge(Settings settings);
}
