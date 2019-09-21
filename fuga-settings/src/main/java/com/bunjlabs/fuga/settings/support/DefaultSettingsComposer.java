package com.bunjlabs.fuga.settings.support;

import com.bunjlabs.fuga.inject.Key;
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
    public <T> T get(Key<?> requester, Key<T> requested) throws SettingsException {
        Assert.notNull(requested);
        Assert.isTrue(requested.getType().isInterface(), "requested type argument must be an interface");

        var requestedClass = requested.getType();
        @SuppressWarnings("unchecked")
        T cached = (T) settingsCache.get(requestedClass);

        if (cached != null) {
            return cached;
        }

        var settingsTree = new DefaultSettingsNode();
        var settingsScopeAnnotation = requestedClass.getAnnotation(Settings.class);
        var scopeName = settingsScopeAnnotation != null ? settingsScopeAnnotation.value() : requestedClass.getSimpleName();

        T proxy = generateProxy(settingsTree.node(scopeName), requestedClass);

        container.persist(settingsTree);
        settingsCache.put(requestedClass, proxy);

        return proxy;
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
                        throw new SettingsException("Default value for method '" + method + "' is empty.");
                    }

                    try {
                        defaultValue = TypeUtils.convertStringToPrimitive(defaultValueString, settingType);
                    } catch (Exception e) {
                        throw new SettingsException("Default value for method '" + method + "' contains unsupported format.", e);
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