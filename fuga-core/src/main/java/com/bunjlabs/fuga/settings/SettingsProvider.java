package com.bunjlabs.fuga.settings;

public interface SettingsProvider {

    <T> T provide(Class<T> requiredSettings) throws SettingsException;
}
