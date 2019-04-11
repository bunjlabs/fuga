package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.settings.settings.SettingsValue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DefaultSettingsHandler implements SettingsHandler {
    private final Map<Method, SettingsValue> wrapperValues;

    DefaultSettingsHandler() {
        this.wrapperValues = new HashMap<>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isObjectMethod(method)) {
            return method.invoke(this, args);
        }

        SettingsValue value = wrapperValues.get(method);

        if (value == null) {
            throw new NullPointerException();
        }

        return value.getValue();
    }

    @Override
    public void addSettingValue(Method method, SettingsValue value) {
        wrapperValues.put(method, value);
    }

    private boolean isObjectMethod(Method m) {
        return Arrays.asList(Object.class.getMethods()).contains(m);

    }


}
