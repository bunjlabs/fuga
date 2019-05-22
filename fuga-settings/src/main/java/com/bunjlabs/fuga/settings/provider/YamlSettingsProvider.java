package com.bunjlabs.fuga.settings.provider;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlSettingsProvider extends JacksonBasedSettingsProvider {
    public YamlSettingsProvider() {
        super(new YAMLFactory());
    }
}
