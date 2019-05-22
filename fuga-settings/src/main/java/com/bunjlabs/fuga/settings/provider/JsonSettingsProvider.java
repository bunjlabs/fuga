package com.bunjlabs.fuga.settings.provider;

import com.fasterxml.jackson.core.JsonFactory;

public class JsonSettingsProvider extends JacksonBasedSettingsProvider {
    public JsonSettingsProvider() {
        super(new JsonFactory());
    }
}
