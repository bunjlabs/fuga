package com.bunjlabs.fuga.settings.settings;

import com.bunjlabs.fuga.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultSettings implements MutableSettings {

    private final Map<String, MutableSettings> nodes;
    private final Map<String, SettingsValue> values;

    public DefaultSettings() {
        this.nodes = new HashMap<>();
        this.values = new HashMap<>();
    }

    @Override
    public MutableSettings node(String name) {
        Assert.notNull(name);

        var settings = nodes.get(name);

        if (settings == null) {
            settings = new DefaultSettings();
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
        return nodes.values().stream().allMatch(Settings::isValuesPresent);
    }

    @Override
    public void set(String key, SettingsValue value) {
        Assert.notNull(key);
        Assert.notNull(value);

        values.put(key, value);
    }

    @Override
    public void setAll(Settings settings) {
        Set<String> keys = settings.keys();
        Set<String> childrenNames = settings.childrenNames();

        keys.forEach(k -> this.set(k, settings.get(k)));
        childrenNames.forEach(c -> this.node(c).setAll(settings.node(c)));
    }

    @Override
    public void merge(Settings settings) {
        Set<String> keys = settings.keys();
        Set<String> childrenNames = settings.childrenNames();

        keys.stream().filter(this::contains).forEach(k ->
                this.get(k).setValue(settings.get(k).getValue())
        );

        childrenNames.stream().filter(this::nodeExists).forEach(c ->
                this.node(c).merge(settings.node(c))
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
