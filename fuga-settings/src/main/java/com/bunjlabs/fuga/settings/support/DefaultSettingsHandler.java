package com.bunjlabs.fuga.settings.support;

import com.bunjlabs.fuga.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultSettingsHandler implements SettingsHandler {
    private final Map<Method, Supplier<?>> suppliers;

    DefaultSettingsHandler() {
        this.suppliers = new HashMap<>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (ReflectionUtils.isObjectMethod(method)) {
            return method.invoke(this, args);
        }

        var value = suppliers.get(method);

        if (value == null) {
            throw new NullPointerException();
        }

        return value.get();
    }

    @Override
    public void putSupplier(Method method, Supplier<?> supplier) {
        suppliers.put(method, supplier);
    }
}
