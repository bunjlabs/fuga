package com.bunjlabs.fuga.settings.loader;

import com.fasterxml.jackson.core.JsonFactory;

public class JsonSettingsLoader extends JacksonBasedSettingsLoader {
    public JsonSettingsLoader() {
        super(new JsonFactory());
    }
}
