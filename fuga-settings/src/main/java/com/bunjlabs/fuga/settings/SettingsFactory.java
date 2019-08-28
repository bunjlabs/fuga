package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.inject.Factory;

public interface SettingsFactory extends Factory {

    <T> T get(Class<T> requiredSettings) throws SettingsException;

}
