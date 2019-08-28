package com.bunjlabs.fuga.examples.minimal.settings;

import com.bunjlabs.fuga.settings.Settings;
import com.bunjlabs.fuga.settings.Value;

@Settings("first")
public interface FirstHttpSettings extends HttpSettings {
    @Value("default")
    String name();
}
