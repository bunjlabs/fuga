package com.bunjlabs.fuga.settings.source;

import com.bunjlabs.fuga.settings.environment.Environment;
import com.bunjlabs.fuga.settings.settings.EmptySettingsNode;
import com.bunjlabs.fuga.settings.settings.SettingsNode;

public class EmptySettingsSource implements SettingsSource {
    @Override
    public SettingsNode getSettings(Environment environment) {
        return EmptySettingsNode.INSTANCE;
    }
}
