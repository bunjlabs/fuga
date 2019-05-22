package com.bunjlabs.fuga.settings.settings;

import java.util.Collections;
import java.util.Set;

public final class EmptySettings implements Settings {

    public static final EmptySettings INSTANCE = new EmptySettings();

    EmptySettings() {
    }

    @Override
    public Settings node(String name) {
        return this;
    }

    @Override
    public boolean nodeExists(String name) {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> childrenNames() {
        return Collections.EMPTY_SET;
    }

    @Override
    public SettingsValue get(String key) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> keys() {
        return Collections.EMPTY_SET;
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public boolean isValuesPresent() {
        return false;
    }
}
