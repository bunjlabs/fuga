package com.bunjlabs.fuga.settings.settings;

import com.bunjlabs.fuga.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class DefaultSettingsScope implements MutableSettingsScope {

    private final Map<String, SettingsValue> values;

    public DefaultSettingsScope() {
        this.values = new HashMap<>();
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

    @Override
    public void set(String key, SettingsValue value) {
        Assert.notNull(key);
        Assert.notNull(value);

        values.put(key, value);
    }
}
