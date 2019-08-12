package com.bunjlabs.fuga.settings.provider;

import com.bunjlabs.fuga.settings.settings.SettingsNode;

import java.io.InputStream;

public interface SettingsProvider {

    SettingsNode load(InputStream is) throws SettingsProviderException;
}
