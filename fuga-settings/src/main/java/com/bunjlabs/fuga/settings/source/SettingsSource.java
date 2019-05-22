package com.bunjlabs.fuga.settings.source;

import com.bunjlabs.fuga.settings.environment.Environment;
import com.bunjlabs.fuga.settings.settings.Settings;

public interface SettingsSource {

    Settings getSettings(Environment environment);
}
