package com.bunjlabs.fuga.settings.settings;

import com.bunjlabs.fuga.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class DefaultHierarchicalSettingsScope extends DefaultSettingsScope implements HierarchicalSettingsScope {

    private final Map<String, HierarchicalSettingsScope> scopes;
    private final Map<String, SettingsValue> values;

    public DefaultHierarchicalSettingsScope() {
        this.scopes = new HashMap<>();
        this.values = new HashMap<>();
    }

    @Override
    public HierarchicalSettingsScope getScope(String name) {
        Assert.notNull(name);

        return scopes.get(name);
    }

    @Override
    public void putScope(String name, HierarchicalSettingsScope scope) {
        Assert.notNull(name);
        Assert.notNull(scope);

        scopes.put(name, scope);
    }

    @Override
    public void set(String key, SettingsValue value) {
        Assert.notNull(key);
        Assert.notNull(value);

        values.put(key, value);
    }

    @Override
    public SettingsValue get(String key) {
        Assert.notNull(key);

        return values.get(key);
    }

    @Override
    public boolean contains(String key) {
        Assert.notNull(key);

        return values.containsKey(key);
    }
}
