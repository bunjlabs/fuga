package com.bunjlabs.fuga.settings.loader;

import com.bunjlabs.fuga.settings.SettingsException;
import com.bunjlabs.fuga.util.Assert;

public class FileExtensionSettingsLoaderSelector implements SettingsLoaderSelector {

    private final YamlSettingsLoader yamlSettingsLoader;
    private final JsonSettingsLoader jsonSettingsLoader;

    public FileExtensionSettingsLoaderSelector(YamlSettingsLoader yamlSettingsLoader, JsonSettingsLoader jsonSettingsLoader) {
        this.yamlSettingsLoader = Assert.notNull(yamlSettingsLoader);
        this.jsonSettingsLoader = Assert.notNull(jsonSettingsLoader);
    }

    @Override
    public SettingsLoader getProvider(String source) {
        if (source.endsWith(".yaml") || source.endsWith(".yml")) {
            return yamlSettingsLoader;
        } else if (source.endsWith(".json")) {
            return jsonSettingsLoader;
        } else {
            throw new SettingsException("Unsupported settings file format");
        }
    }
}
