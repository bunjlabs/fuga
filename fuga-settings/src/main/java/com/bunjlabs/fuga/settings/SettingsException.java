package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.inject.ProvisionException;

public class SettingsException extends ProvisionException {
    public SettingsException() {
    }

    public SettingsException(String msg) {
        super(msg);
    }

    public SettingsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
