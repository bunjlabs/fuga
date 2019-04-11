package com.bunjlabs.fuga.settings.settings;

public interface HierarchicalSettingsScope extends MutableSettingsScope {

    HierarchicalSettingsScope getScope(String name);

    void putScope(String name, HierarchicalSettingsScope scope);
}
