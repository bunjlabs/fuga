package fuga.settings.support;

import fuga.common.annotation.AnnotationUtils;
import fuga.inject.Binding;
import fuga.inject.ConfigurationException;
import fuga.inject.Inject;
import fuga.settings.*;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

public class DefaultSettingsConfigurator implements SettingsConfigurator {

    private final SettingsContainer container;

    @Inject
    public DefaultSettingsConfigurator(SettingsContainer container) {
        this.container = container;
    }

    @Override
    public void configure(Binding<Object> binding) throws ConfigurationException {
        var type = binding.getKey().getType();
        if (!type.isInterface()) {
            throw new ConfigurationException(
                    String.format("Settings %s must be interface.", type));
        }

        var typeClass = type.getRawType();
        var annotation = AnnotationUtils.findAnnotation(typeClass, Settings.class);
        if (annotation == null) {
            throw new ConfigurationException(
                    String.format("Settings %s must be annotated with %s annotation.", type, Settings.class));
        }

        var settingsTree = new DefaultSettingsNode();
        var scopeName = annotation.value().isEmpty() ? typeClass.getSimpleName() : annotation.value();
        var proxy = generateProxy(settingsTree.node(scopeName), typeClass);

        container.persist(settingsTree);

        binding.setAttribute(SettingsAgent.class, new SettingsAgent(typeClass, proxy));
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
