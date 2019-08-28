package com.bunjlabs.fuga.settings.loader;

import com.bunjlabs.fuga.settings.SettingsNode;

import java.io.InputStream;

public interface SettingsLoader {

    SettingsNode load(InputStream is) throws SettingsLoaderException;
}
