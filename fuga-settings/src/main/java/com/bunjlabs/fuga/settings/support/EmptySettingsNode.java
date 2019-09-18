package com.bunjlabs.fuga.settings.support;

import com.bunjlabs.fuga.settings.SettingsNode;
import com.bunjlabs.fuga.settings.SettingsValue;

import java.util.Collections;
import java.util.Set;

public final class EmptySettingsNode implements SettingsNode {

    public static final EmptySettingsNode INSTANCE = new EmptySettingsNode();

    EmptySettingsNode() {
    }

    @Override
    public SettingsNode node(String name) {
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
}
