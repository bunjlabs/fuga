package com.bunjlabs.fuga.settings;

import java.util.Set;

public interface SettingsNode {

    SettingsNode node(String name);

    boolean nodeExists(String name);

    Set<String> childrenNames();

    SettingsValue get(String key);

    Set<String> keys();

    boolean contains(String key);

    boolean isValuesPresent();
}
