package com.bunjlabs.fuga.settings;

public interface SettingsValue {

    Class type();

    Object value();

    void value(Object value);

    Object defaultValue();

    void defaultValue(Object defaultValue);

    boolean isValuePresent();
}
