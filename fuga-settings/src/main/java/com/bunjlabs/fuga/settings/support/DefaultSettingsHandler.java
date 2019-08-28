package com.bunjlabs.fuga.settings.support;

import com.bunjlabs.fuga.settings.SettingsHandler;
import com.bunjlabs.fuga.settings.SettingsValue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DefaultSettingsHandler implements SettingsHandler {
    private final Map<Method, SettingsValue> wrappedValues;

    DefaultSettingsHandler() {
        this.wrappedValues = new HashMap<>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isObjectMethod(method)) {
            return method.invoke(this, args);
        }

        var value = wrappedValues.get(method);

        if (value == null) {
            throw new NullPointerException();
        }

        return value.getValue();
    }

    @Override
    public void addSettingValue(Method method, SettingsValue value) {
        wrappedValues.put(method, value);
    }

    private boolean isObjectMethod(Method m) {
        return Arrays.asList(Object.class.getMethods()).contains(m);
    }


}
