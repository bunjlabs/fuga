package com.bunjlabs.fuga.settings.settings;

public interface MutableSettingsScope extends SettingsScope {

    void set(String key, SettingsValue value);
}
