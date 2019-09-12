package com.bunjlabs.fuga.settings.source;

import com.bunjlabs.fuga.environment.Environment;
import com.bunjlabs.fuga.settings.SettingsNode;

public interface SettingsSource {

    SettingsNode getSettings(Environment environment);
}
