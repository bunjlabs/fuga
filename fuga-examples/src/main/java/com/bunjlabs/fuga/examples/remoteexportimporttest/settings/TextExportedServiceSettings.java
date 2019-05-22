package com.bunjlabs.fuga.examples.remoteexportimporttest.settings;

import com.bunjlabs.fuga.settings.annotations.SettingsScope;

@SettingsScope("test")
public interface TextExportedServiceSettings {
    int intValue();

    String stringValue();
}
