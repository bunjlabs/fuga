package com.bunjlabs.fuga.settings.support;

import com.bunjlabs.fuga.settings.SettingsException;
import com.bunjlabs.fuga.settings.SettingsFactory;
import com.bunjlabs.fuga.settings.SettingsHandler;
import com.bunjlabs.fuga.settings.annotations.Settings;
import com.bunjlabs.fuga.settings.annotations.Value;
import com.bunjlabs.fuga.settings.environment.Environment;
import com.bunjlabs.fuga.settings.settings.*;
import com.bunjlabs.fuga.settings.source.SettingsSource;
import com.bunjlabs.fuga.util.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DefaultSettingsFactory implements SettingsFactory {

    private final MutableSettingsNode rootNode;

    public DefaultSettingsFactory() {
        this.rootNode = new DefaultSettingsNode();
    }

    @Override
    public <T> T provide(Class<T> requiredSettings) throws SettingsException {
        Assert.notNull(requiredSettings);
        Assert.isTrue(requiredSettings.isInterface(), "requiredSettings argument must be an interface");

        var invocationHandler = new DefaultSettingsHandler();

        populateValuesForHandler(rootNode, invocationHandler, requiredSettings);

        return generateProxy(requiredSettings, invocationHandler);
    }

    public void loadFromSettingsSource(SettingsSource settingsSource, Environment environment) {
        Assert.notNull(settingsSource);
        Assert.notNull(environment);

        var settings = settingsSource.getSettings(environment);

        rootNode.merge(settings);
    }

    public boolean checkAvailibility() {
        return rootNode.isValuesPresent();
    }

    @SuppressWarnings("unchecked")
    private <T> T generateProxy(Class<T> requiredSettings, InvocationHandler invocationHandler) throws SettingsException {
        var proxyInstance = Proxy.newProxyInstance(requiredSettings.getClassLoader(), new Class[]{requiredSettings}, invocationHandler);

        return (T) proxyInstance;
    }

    private <T> void populateValuesForHandler(MutableSettingsNode currentNode, SettingsHandler invocationHandler, Class<T> requiredSettings) {
        var settingsScopeAnnotation = requiredSettings.getAnnotation(Settings.class);
        var scopeName = settingsScopeAnnotation != null ? settingsScopeAnnotation.value() : requiredSettings.getSimpleName();

        currentNode = currentNode.node(scopeName);

        for (Method method : requiredSettings.getDeclaredMethods()) {
            Class<?> returnType = method.getReturnType();
            Object defaultValue = null;

            var defaultValueAnnotation = method.getAnnotation(Value.class);

            if (defaultValueAnnotation != null) {
                String defaultValueString = defaultValueAnnotation.value();
                if (defaultValueString.isEmpty()) {
                    throw new SettingsException("Default value of method :'" + method + "' is empty.");
                }

                try {
                    defaultValue = TypeUtils.convertStringToPrimitive(defaultValueString, returnType);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    throw new SettingsException("Method :'" + method + "' provide default value with unsupported format.", e);
                }
            }

            var settingsValue = new DefaultSettingsValue(method.getReturnType(), defaultValue);

            currentNode.set(method.getName(), settingsValue);
            invocationHandler.addSettingValue(method, settingsValue);
        }

        for (Class<?> innerSettings : requiredSettings.getInterfaces()) {
            populateValuesForHandler(currentNode, invocationHandler, innerSettings);
        }
    }
}