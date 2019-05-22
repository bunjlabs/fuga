package com.bunjlabs.fuga.settings.source;

import com.bunjlabs.fuga.settings.environment.Environment;
import com.bunjlabs.fuga.settings.settings.EmptySettings;
import com.bunjlabs.fuga.settings.settings.Settings;

public class EmptySettingsSource implements SettingsSource {
    @Override
    public Settings getSettings(Environment environment) {
        return EmptySettings.INSTANCE;
    }
}
