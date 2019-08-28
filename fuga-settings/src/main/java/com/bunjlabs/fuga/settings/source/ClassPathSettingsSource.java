package com.bunjlabs.fuga.settings.source;

import com.bunjlabs.fuga.settings.environment.Environment;
import com.bunjlabs.fuga.settings.environment.EnvironmentException;
import com.bunjlabs.fuga.settings.loader.*;
import com.bunjlabs.fuga.settings.support.settings.DefaultSettingsNode;
import com.bunjlabs.fuga.settings.SettingsNode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class ClassPathSettingsSource implements SettingsSource {

    private final ClassLoader classLoader;
    private final Iterable<String> resourceNames;
    private final SettingsLoaderSelector providerSelector;

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
        this(classLoader, resourceNames, new FileExtensionSettingsLoaderSelector(
                new YamlSettingsLoader(),
                new JsonSettingsLoader()
        ));
    }

    public ClassPathSettingsSource(ClassLoader classLoader, Iterable<String> resourceNames, SettingsLoaderSelector providerSelector) {
        this.classLoader = classLoader;
        this.resourceNames = resourceNames;
        this.providerSelector = providerSelector;
    }

    public SettingsNode getSettings(Environment environment) {
        var settings = new DefaultSettingsNode();

        for (String resource : resourceNames) {
            try (InputStream is = classLoader.getResourceAsStream(resource)) {

                if (is == null) {
                    throw new FileNotFoundException();
                }

                SettingsLoader settingsLoader = providerSelector.getProvider(resource);
                settings.setAll(settingsLoader.load(is));
            } catch (FileNotFoundException e) {
                throw new EnvironmentException("Settings file doesn't exist: " + resource);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to load settings file: " + resource);
            }
        }

        return settings;
    }
}
