package com.bunjlabs.fuga.settings.source;

import com.bunjlabs.fuga.settings.environment.Environment;
import com.bunjlabs.fuga.settings.settings.SettingsNode;

public interface SettingsSource {

    SettingsNode getSettings(Environment environment);
}
