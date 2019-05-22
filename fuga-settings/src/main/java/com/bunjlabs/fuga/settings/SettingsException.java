package com.bunjlabs.fuga.settings;

public class SettingsException extends RuntimeException {
    public SettingsException(String msg) {
        super(msg);
    }

    public SettingsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
