package com.bunjlabs.fuga.settings.loader;

public interface SettingsLoaderSelector {

    SettingsLoader getProvider(String source);
}
