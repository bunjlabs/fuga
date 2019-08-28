package com.bunjlabs.fuga.settings;

public interface SettingsValue {

    Class getType();

    Object getValue();

    void setValue(Object value);

    void setDefaultValue(Object defaultValue);

    boolean isValuePresent();
}
