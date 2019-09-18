package com.bunjlabs.fuga.settings.support;

import com.bunjlabs.fuga.settings.*;
import com.bunjlabs.fuga.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultSettingsComposer implements SettingsComposer {

    private final SettingsContainer container;
    private final Map<Class<?>, Object> settingsCache;

    public DefaultSettingsComposer(SettingsContainer container) {
        this.container = container;
        this.settingsCache = new HashMap<>();
    }

    @Override
    public <T> T get(Class<T> requiredClass) throws SettingsException {
        Assert.notNull(requiredClass);
        Assert.isTrue(requiredClass.isInterface(), "requiredSettings argument must be an interface");

        @SuppressWarnings("unchecked")
        T cached = (T) settingsCache.get(requiredClass);

        if (cached != null) {
            return cached;
        }

        var settingsTree = new DefaultSettingsNode();
        var settingsScopeAnnotation = requiredClass.getAnnotation(Settings.class);
        var scopeName = settingsScopeAnnotation != null ? settingsScopeAnnotation.value() : requiredClass.getSimpleName();

        T t = generateProxy(settingsTree.node(scopeName), requiredClass);

        container.persist(settingsTree);
        settingsCache.put(requiredClass, t);

        return t;
    }

    @SuppressWarnings("unchecked")
    private <T> T generateProxy(MutableSettingsNode settingsTree, Class<T> requiredClass) throws SettingsException {
        var handler = new DefaultSettingsHandler();
        fillHandlerWithValues(settingsTree, handler, requiredClass);
        return (T) Proxy.newProxyInstance(requiredClass.getClassLoader(), new Class[]{requiredClass}, handler);
    }

    private <T> void fillHandlerWithValues(MutableSettingsNode currentNode, SettingsHandler invocationHandler, Class<T> requiredSettings) {
        for (Method method : requiredSettings.getDeclaredMethods()) {
            var settingNameAnnotation = method.getAnnotation(SettingName.class);
            var settingDefaultAnnotation = method.getAnnotation(SettingDefault.class);
            var settingName = settingNameAnnotation != null ? settingNameAnnotation.value() : method.getName();
            var settingType = method.getReturnType();
            Object defaultValue = null;

            if (!settingType.isInterface() || Collection.class.isAssignableFrom(settingType)) {
                if (settingDefaultAnnotation != null) {
                    String defaultValueString = settingDefaultAnnotation.value();
                    if (defaultValueString.isEmpty()) {
                        throw new SettingsException("Default merge of method '" + method + "' is empty.");
                    }

                    try {
                        defaultValue = TypeUtils.convertStringToPrimitive(defaultValueString, settingType);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        throw new SettingsException("Method '" + method + "' provide default merge with unsupported format.", e);
                    }
                }

                var settingsValue = new DefaultSettingsValue(method.getReturnType(), defaultValue);
                currentNode.set(settingName, settingsValue);

                invocationHandler.putSupplier(method, settingsValue::value);
            } else {
                var value = generateProxy(currentNode.node(settingName), settingType);
                invocationHandler.putSupplier(method, () -> value);
            }

            for (Class<?> innerSettings : requiredSettings.getInterfaces()) {
                fillHandlerWithValues(currentNode, invocationHandler, innerSettings);
            }
        }
    }
}