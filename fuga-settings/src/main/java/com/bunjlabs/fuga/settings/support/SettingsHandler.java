package com.bunjlabs.fuga.settings.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public interface SettingsHandler extends InvocationHandler {

    void putSupplier(Method method, Supplier<?> supplier);
}
