package com.bunjlabs.fuga.settings.settings;

public class DefaultSettingsValue implements SettingsValue {

    private final Class<?> type;
    private final Object defaultValue;
    private Object value;

    public DefaultSettingsValue(Class<?> type, Object defaultValue) {
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public DefaultSettingsValue(Class<?> type, Object value, Object defaultValue) {
        this.type = type;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public Object getValue() {
        return value != null ? value : defaultValue;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }
}
