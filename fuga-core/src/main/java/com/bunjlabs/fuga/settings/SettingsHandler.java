package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.settings.settings.SettingsValue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public interface SettingsHandler extends InvocationHandler {

    void addSettingValue(Method method, SettingsValue value);
}
