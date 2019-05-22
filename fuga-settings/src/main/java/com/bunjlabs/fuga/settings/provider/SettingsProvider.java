package com.bunjlabs.fuga.settings.provider;

import com.bunjlabs.fuga.settings.settings.Settings;

import java.io.InputStream;

public interface SettingsProvider {

    Settings load(InputStream is) throws SettingsProviderException;
}
