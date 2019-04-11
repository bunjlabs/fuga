package com.bunjlabs.fuga.settings.settings;

public interface SettingsScope {

    SettingsValue get(String key);

    boolean contains(String key);
}
