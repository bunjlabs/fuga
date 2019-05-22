package com.bunjlabs.fuga.settings.provider;

import com.bunjlabs.fuga.settings.SettingsException;
import com.bunjlabs.fuga.util.Assert;

public class FileExtensionSettingsProviderSelector implements SettingsProviderSelector {

    private final YamlSettingsProvider yamlSettingsLoader;
    private final JsonSettingsProvider jsonSettingsLoader;

    public FileExtensionSettingsProviderSelector(YamlSettingsProvider yamlSettingsLoader, JsonSettingsProvider jsonSettingsLoader) {
        this.yamlSettingsLoader = Assert.notNull(yamlSettingsLoader);
        this.jsonSettingsLoader = Assert.notNull(jsonSettingsLoader);
    }

    @Override
    public SettingsProvider getProvider(String source) {
        if (source.endsWith(".yaml") || source.endsWith(".yml")) {
            return yamlSettingsLoader;
        } else if (source.endsWith(".json")) {
            return jsonSettingsLoader;
        } else {
            throw new SettingsException("Unsupported settings file format");
        }
    }
}
