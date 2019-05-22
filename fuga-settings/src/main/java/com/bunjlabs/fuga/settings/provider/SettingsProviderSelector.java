package com.bunjlabs.fuga.settings.provider;

public interface SettingsProviderSelector {

    SettingsProvider getProvider(String source);
}
