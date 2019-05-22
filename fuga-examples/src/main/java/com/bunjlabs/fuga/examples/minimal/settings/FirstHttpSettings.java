package com.bunjlabs.fuga.examples.minimal.settings;

import com.bunjlabs.fuga.settings.annotations.SettingsScope;
import com.bunjlabs.fuga.settings.annotations.Value;

@SettingsScope("first")
public interface FirstHttpSettings extends HttpSettings {
    @Value("default")
    String name();
}
