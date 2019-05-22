package com.bunjlabs.fuga.settings.settings;

import java.util.Set;

public interface Settings {

    Settings node(String name);

    boolean nodeExists(String name);

    Set<String> childrenNames();

    SettingsValue get(String key);

    Set<String> keys();

    boolean contains(String key);

    boolean isValuesPresent();
}
