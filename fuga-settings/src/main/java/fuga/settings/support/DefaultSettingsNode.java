/*
 * Copyright 2019-2021 Bunjlabs
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

package fuga.settings.support;

import fuga.settings.MutableSettingsNode;
import fuga.settings.SettingsNode;
import fuga.settings.SettingsValue;
import fuga.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultSettingsNode implements MutableSettingsNode {

    private final Map<String, MutableSettingsNode> nodes;
    private final Map<String, SettingsValue> values;

    public DefaultSettingsNode() {
        this.nodes = new HashMap<>();
        this.values = new HashMap<>();
    }

    @Override
    public MutableSettingsNode node(String name) {
        Assert.notNull(name);

        var settings = nodes.get(name);

        if (settings == null) {
            settings = new DefaultSettingsNode();
            nodes.put(name, settings);
        }

        return settings;
    }

    @Override
    public boolean nodeExists(String name) {
        return nodes.containsKey(name);
    }

    @Override
    public Set<String> childrenNames() {
        return nodes.keySet();
    }

    @Override
    public void set(String key, SettingsValue value) {
        Assert.notNull(key);
        Assert.notNull(value);
        values.put(key, value);
    }

    @Override
    public void setAll(SettingsNode settingsNode) {
        Set<String> keys = settingsNode.keys();
        Set<String> childrenNames = settingsNode.childrenNames();

        keys.forEach(k -> this.set(k, new DefaultSettingsValue(settingsNode.get(k))));
        childrenNames.forEach(c -> this.node(c).setAll(settingsNode.node(c)));
    }

    @Override
    public void merge(SettingsNode settingsNode) {
        Set<String> keys = settingsNode.keys();
        Set<String> childrenNames = settingsNode.childrenNames();

        keys.stream().filter(this::contains).forEach(k ->
                this.get(k).value(settingsNode.get(k).value())
        );

        childrenNames.stream().filter(this::nodeExists).forEach(c ->
                this.node(c).merge(settingsNode.node(c))
        );
    }

    @Override
    public SettingsValue get(String key) {
        Assert.notNull(key);
        return values.get(key);
    }

    @Override
    public Set<String> keys() {
        return values.keySet();
    }

    @Override
    public boolean contains(String key) {
        Assert.notNull(key);
        return values.containsKey(key);
    }
}
