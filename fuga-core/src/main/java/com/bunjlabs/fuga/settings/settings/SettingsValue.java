package com.bunjlabs.fuga.settings.settings;

public interface SettingsValue {

    Class getType();

    Object getValue();

    void setValue(Object value);

    Object getDefaultValue();
}
