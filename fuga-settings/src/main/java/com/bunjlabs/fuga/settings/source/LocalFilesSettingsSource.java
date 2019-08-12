package com.bunjlabs.fuga.settings.source;

import com.bunjlabs.fuga.settings.environment.Environment;
import com.bunjlabs.fuga.settings.environment.EnvironmentException;
import com.bunjlabs.fuga.settings.provider.*;
import com.bunjlabs.fuga.settings.settings.DefaultSettings;
import com.bunjlabs.fuga.settings.settings.MutableSettings;
import com.bunjlabs.fuga.settings.settings.Settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LocalFilesSettingsSource implements SettingsSource {

    private final Path rootPath;
    private final Iterable<Path> settingFiles;
    private final SettingsProviderSelector providerSelector;

    public LocalFilesSettingsSource() {
        this(Paths.get(System.getProperty("user.home")));
    }

    public LocalFilesSettingsSource(String root) {
        this(Paths.get(root));
    }

    public LocalFilesSettingsSource(Path root) {
        this(root, Collections.singletonList(Paths.get("settings.yaml")));
    }

    public LocalFilesSettingsSource(String root, String... settingFiles) {
        this(root, Arrays.stream(settingFiles).map(s -> Paths.get(s)).collect(Collectors.toList()));
    }


    public LocalFilesSettingsSource(String root, Path... settingFiles) {
        this(root, Arrays.asList(settingFiles));
    }

    public LocalFilesSettingsSource(String root, Iterable<Path> settingFiles) {
        this(Paths.get(root), settingFiles);
    }

    public LocalFilesSettingsSource(Path root, Path... settingFiles) {
        this(root, Arrays.asList(settingFiles));
    }

    public LocalFilesSettingsSource(Path root, Iterable<Path> settingFiles) {
        this(root, settingFiles, new FileExtensionSettingsProviderSelector(
                new YamlSettingsProvider(),
                new JsonSettingsProvider()
        ));
    }

    public LocalFilesSettingsSource(Path root, Iterable<Path> settingFiles, SettingsProviderSelector providerSelector) {
        this.rootPath = root;
        this.settingFiles = settingFiles;
        this.providerSelector = providerSelector;
    }

    @Override
    public Settings getSettings(Environment environment) {
        Path settingsPath;

        if (environment.getName().trim().isEmpty()) {
            settingsPath = rootPath;
        } else {
            settingsPath = rootPath.resolve(environment.getName());
        }

        if (!settingsPath.toFile().exists()) {
            throw new EnvironmentException("Directory doesn't exist: " + settingsPath);
        }

        var settingsPaths = new ArrayList<Path>();
        for (Path path : settingFiles) {
            settingsPaths.add(settingsPath.resolve(path));
        }

        var settings = new DefaultSettings();

        for (Path path : settingsPaths) {
            try (InputStream is = new FileInputStream(path.toFile())) {

                SettingsProvider settingsProvider = providerSelector.getProvider(path.getFileName().toString());
                settings.setAll(settingsProvider.load(is));

            } catch (FileNotFoundException e) {
                throw new EnvironmentException("SettingsScope file doesn't exist: " + path);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to load settings file: " + path);
            }
        }

        return settings;
    }
}
