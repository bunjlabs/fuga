package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.inject.Composer;

public interface SettingsComposer extends Composer {

    <T> T get(Class<?> requester, Class<T> requiredClass) throws SettingsException;

}
