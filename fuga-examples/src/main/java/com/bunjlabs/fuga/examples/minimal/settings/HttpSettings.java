package com.bunjlabs.fuga.examples.minimal.settings;

import com.bunjlabs.fuga.settings.Settings;
import com.bunjlabs.fuga.settings.SettingDefault;

@Settings("http")
public interface HttpSettings {

    @SettingDefault("localhost")
    String host();

    @SettingDefault("8080")
    int port();
}
