package com.bunjlabs.fuga.settings.source;

import com.bunjlabs.fuga.environment.Environment;
import com.bunjlabs.fuga.settings.support.EmptySettingsNode;
import com.bunjlabs.fuga.settings.SettingsNode;

public class EmptySettingsSource implements SettingsSource {
    @Override
    public SettingsNode getSettings(Environment environment) {
        return EmptySettingsNode.INSTANCE;
    }
}
