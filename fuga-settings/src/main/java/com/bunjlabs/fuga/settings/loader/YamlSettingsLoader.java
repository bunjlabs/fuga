package com.bunjlabs.fuga.settings.loader;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlSettingsLoader extends JacksonBasedSettingsLoader {
    public YamlSettingsLoader() {
        super(new YAMLFactory());
    }
}
