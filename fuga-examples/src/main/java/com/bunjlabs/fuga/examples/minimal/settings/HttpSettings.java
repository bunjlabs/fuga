package com.bunjlabs.fuga.examples.minimal.settings;

import com.bunjlabs.fuga.settings.annotations.SettingsScope;
import com.bunjlabs.fuga.settings.annotations.Value;

@SettingsScope("http")
public interface HttpSettings {

    @Value("localhost")
    String host();

    @Value("8080")
    int port();
}
