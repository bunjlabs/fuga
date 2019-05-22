package com.bunjlabs.fuga.settings;

public interface SettingsFactory {

    <T> T provide(Class<T> requiredSettings) throws SettingsException;
}
