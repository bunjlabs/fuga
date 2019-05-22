package com.bunjlabs.fuga.settings.provider;

import com.bunjlabs.fuga.settings.SettingsException;

public class SettingsProviderException extends SettingsException {
    public SettingsProviderException(String msg) {
        super(msg);
    }

    public SettingsProviderException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
