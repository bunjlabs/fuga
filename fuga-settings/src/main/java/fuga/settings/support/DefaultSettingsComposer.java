/*
 * Copyright 2019-2021 Bunjlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fuga.settings.support;

import fuga.inject.Key;
import fuga.settings.*;
import fuga.util.Assert;

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
        Assert.isTrue(requested.getRawType().isInterface(), "requested type argument must be an interface");

        var requestedClass = requested.getRawType();
        @SuppressWarnings("unchecked")
        var cached = (T) settingsCache.get(requestedClass);

        if (cached != null) {
            return cached;
        }

        var settingsTree = new DefaultSettingsNode();
        var settingsScopeAnnotation = requestedClass.getAnnotation(Settings.class);
        var scopeName = settingsScopeAnnotation != null ? settingsScopeAnnotation.value() : requestedClass.getSimpleName();

        if (scopeName.isEmpty()) {
            scopeName = requestedClass.getSimpleName();
        }

        var proxy = generateProxy(settingsTree.node(scopeName), requestedClass);

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

            for (var innerSettings : requiredSettings.getInterfaces()) {
                fillHandlerWithValues(currentNode, invocationHandler, innerSettings);
            }
        }
    }
}