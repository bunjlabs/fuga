package com.bunjlabs.fuga.examples.minimal.settings;

import com.bunjlabs.fuga.settings.Settings;
import com.bunjlabs.fuga.settings.Value;

@Settings("http")
public interface HttpSettings {

    @Value("localhost")
    String host();

    @Value("8080")
    int port();
}
