package com.bunjlabs.fuga.settings.source;

import com.bunjlabs.fuga.settings.environment.Environment;
import com.bunjlabs.fuga.settings.environment.EnvironmentException;
import com.bunjlabs.fuga.settings.provider.*;
import com.bunjlabs.fuga.settings.settings.DefaultSettings;
import com.bunjlabs.fuga.settings.settings.MutableSettings;
import com.bunjlabs.fuga.settings.settings.Settings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class ClassPathSettingsSource implements SettingsSource {

    private final ClassLoader classLoader;
    private final Iterable<String> resourceNames;
    private final SettingsProviderSelector providerSelector;

    public ClassPathSettingsSource() {
        this(SettingsSource.class.getClassLoader());
    }

    public ClassPathSettingsSource(String... resourceNames) {
        this(SettingsSource.class.getClassLoader(), Arrays.stream(resourceNames).collect(Collectors.toList()));
    }

    public ClassPathSettingsSource(Iterable<String> resourceNames) {
        this(SettingsSource.class.getClassLoader(), resourceNames);
    }

    public ClassPathSettingsSource(ClassLoader classLoader) {
        this(classLoader, Collections.singletonList("settings.yaml"));
    }

    public ClassPathSettingsSource(ClassLoader classLoader, String... resourceNames) {
        this(classLoader, Arrays.stream(resourceNames).collect(Collectors.toList()));
    }

    public ClassPathSettingsSource(ClassLoader classLoader, Iterable<String> resourceNames) {
        this(classLoader, resourceNames, new FileExtensionSettingsProviderSelector(
                new YamlSettingsProvider(),
                new JsonSettingsProvider()
        ));
    }

    public ClassPathSettingsSource(ClassLoader classLoader, Iterable<String> resourceNames, SettingsProviderSelector providerSelector) {
        this.classLoader = classLoader;
        this.resourceNames = resourceNames;
        this.providerSelector = providerSelector;
    }

    public Settings getSettings(Environment environment) {
        var settings = new DefaultSettings();

        for (String resource : resourceNames) {
            try (InputStream is = classLoader.getResourceAsStream(resource)) {

                if (is == null) {
                    throw new FileNotFoundException();
                }

                SettingsProvider settingsProvider = providerSelector.getProvider(resource);
                settings.setAll(settingsProvider.load(is));
            } catch (FileNotFoundException e) {
                throw new EnvironmentException("SettingsScope file doesn't exist: " + resource);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to load settings file: " + resource);
            }
        }

        return settings;
    }
}
