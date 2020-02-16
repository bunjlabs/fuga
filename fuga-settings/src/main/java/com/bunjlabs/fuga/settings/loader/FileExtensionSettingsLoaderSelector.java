/*
 * Copyright 2019-2020 Bunjlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
