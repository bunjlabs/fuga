package com.bunjlabs.fuga.examples.minimal.settings;

import com.bunjlabs.fuga.settings.SettingDefault;
import com.bunjlabs.fuga.settings.Settings;

import java.util.List;

@Settings("first")
public interface FirstHttpSettings {
    @SettingDefault("default")
    String name();

    HttpSettings first();

    List<String> profiles();
}
