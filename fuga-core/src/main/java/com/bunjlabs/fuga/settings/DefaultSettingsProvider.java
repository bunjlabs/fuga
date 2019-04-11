package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.settings.annotations.Settings;
import com.bunjlabs.fuga.settings.annotations.Value;
import com.bunjlabs.fuga.settings.settings.DefaultHierarchicalSettingsScope;
import com.bunjlabs.fuga.settings.settings.DefaultSettingsValue;
import com.bunjlabs.fuga.settings.settings.HierarchicalSettingsScope;
import com.bunjlabs.fuga.settings.settings.SettingsValue;
import com.bunjlabs.fuga.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DefaultSettingsProvider implements SettingsProvider {

    private final HierarchicalSettingsScope rootScope;

    public DefaultSettingsProvider() {
        this.rootScope = new DefaultHierarchicalSettingsScope();
    }

    @Override
    public <T> T provide(Class<T> requiredSettings) throws SettingsException {
        Assert.notNull(requiredSettings);
        Assert.isTrue(requiredSettings.isInterface(), "requiredSettings argument must be an interface");

        SettingsHandler invocationHandler = new DefaultSettingsHandler();

        populateValuesForHandler(rootScope, invocationHandler, requiredSettings);

        return generateProxy(requiredSettings, invocationHandler);
    }

    @SuppressWarnings("unchecked")
    private <T> T generateProxy(Class<T> requiredSettings, InvocationHandler invocationHandler) throws SettingsException {
        Object proxyInstance = Proxy.newProxyInstance(requiredSettings.getClassLoader(), new Class[]{requiredSettings}, invocationHandler);

        return (T) proxyInstance;
    }

    private <T> void populateValuesForHandler(HierarchicalSettingsScope currentScope, SettingsHandler invocationHandler, Class<T> requiredSettings) {
        Settings settingsAnnotation = requiredSettings.getAnnotation(Settings.class);
        String scopeName = settingsAnnotation != null ? settingsAnnotation.value() : requiredSettings.getSimpleName();

        HierarchicalSettingsScope nextScope = new DefaultHierarchicalSettingsScope();

        currentScope.putScope(scopeName, nextScope);

        currentScope = nextScope;

        for (Method method : requiredSettings.getDeclaredMethods()) {
            Value defaultValueAnnotation = method.getAnnotation(Value.class);

            if (defaultValueAnnotation == null) {
                throw new SettingsException("Method :'" + method + "' must provide default value with Value annotation.");
            }

            Object defaultValue = defaultValueAnnotation.value();

            SettingsValue settingsValue = new DefaultSettingsValue(method.getReturnType(), defaultValue);

            currentScope.set(method.getName(), settingsValue);
            invocationHandler.addSettingValue(method, settingsValue);
        }

        for (Class<?> innerSettings : requiredSettings.getInterfaces()) {
            populateValuesForHandler(currentScope, invocationHandler, innerSettings);
        }
    }
}