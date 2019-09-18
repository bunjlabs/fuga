package com.bunjlabs.fuga.settings.support;

import com.bunjlabs.fuga.settings.SettingsValue;

public class DefaultSettingsValue implements SettingsValue {

    private final Class<?> type;
    private Object defaultValue;
    private Object value;

    public DefaultSettingsValue(Class<?> type) {
        this.type = type;
    }

    public DefaultSettingsValue(Class<?> type, Object value) {
        this.type = type;
        this.value = value;
    }

    public DefaultSettingsValue(Class<?> type, Object value, Object defaultValue) {
        this.type = type;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    public DefaultSettingsValue(SettingsValue value) {
        this.type = value.type();
        this.defaultValue = value.defaultValue();
        this.value = value.value();
    }

    @Override
    public Class<?> type() {
        return type;
    }

    @Override
    public Object value() {
        return value != null ? value : defaultValue;
    }

    @Override
    public void value(Object value) {
        this.value = castToThis(value);
    }

    @Override
    public Object defaultValue() {
        return this.defaultValue;
    }

    @Override
    public void defaultValue(Object defaultValue) {
        this.defaultValue = castToThis(defaultValue);
    }

    @Override
    public boolean isValuePresent() {
        return value != null || defaultValue != null;
    }

    private Object castToThis(Object value) {
        if (type.isAssignableFrom(value.getClass())) return value;

        if (value instanceof Number) {
            var num = (Number) value;
            if (type == int.class || type == Integer.class) {
                return num.intValue();
            } else if (type == long.class || type == Long.class) {
                return num.longValue();
            } else if (type == float.class || type == Float.class) {
                return num.floatValue();
            } else if (type == double.class || type == Double.class) {
                return num.doubleValue();
            } else if (type == byte.class || type == Byte.class) {
                return num.byteValue();
            } else if (type == short.class || type == Short.class) {
                return num.byteValue();
            }
        }

        return type.cast(value);
    }
}
