package com.bunjlabs.fuga.examples.remoteexportimporttest.settings;

import com.bunjlabs.fuga.settings.annotations.Settings;

@Settings("test")
public interface TextExportedServiceSettings {
    int intValue();

    String stringValue();
}
