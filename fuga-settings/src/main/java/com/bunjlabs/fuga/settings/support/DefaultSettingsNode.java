package com.bunjlabs.fuga.settings.support;

import com.bunjlabs.fuga.settings.MutableSettingsNode;
import com.bunjlabs.fuga.settings.SettingsNode;
import com.bunjlabs.fuga.settings.SettingsValue;
import com.bunjlabs.fuga.util.Assert;

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
    public boolean isValuesPresent() {
        if (!values.values().stream().allMatch(SettingsValue::isValuePresent)) return false;
        return nodes.values().stream().allMatch(SettingsNode::isValuesPresent);
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
