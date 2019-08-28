package com.bunjlabs.fuga.settings.support.settings;

import com.bunjlabs.fuga.settings.SettingsNode;
import com.bunjlabs.fuga.settings.SettingsValue;

public interface MutableSettingsNode extends SettingsNode {

    MutableSettingsNode node(String name);

    void set(String key, SettingsValue value);

    void setAll(SettingsNode settingsNode);

    void merge(SettingsNode settingsNode);
}
