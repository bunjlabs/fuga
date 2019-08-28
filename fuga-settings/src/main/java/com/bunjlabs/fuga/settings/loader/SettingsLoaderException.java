package com.bunjlabs.fuga.settings.loader;

import com.bunjlabs.fuga.settings.SettingsException;

public class SettingsLoaderException extends SettingsException {
    public SettingsLoaderException(String msg) {
        super(msg);
    }

    public SettingsLoaderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
