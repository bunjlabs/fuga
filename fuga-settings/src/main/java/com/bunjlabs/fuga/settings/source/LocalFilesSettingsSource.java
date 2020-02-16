/*
 * Copyright 2019-2020 Bunjlabs
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

package com.bunjlabs.fuga.settings.source;

import com.bunjlabs.fuga.environment.Environment;
import com.bunjlabs.fuga.environment.EnvironmentException;
import com.bunjlabs.fuga.settings.loader.*;
import com.bunjlabs.fuga.settings.support.DefaultSettingsNode;
import com.bunjlabs.fuga.settings.SettingsNode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class LocalFilesSettingsSource implements SettingsSource {

    private final Path rootPath;
    private final Iterable<Path> settingFiles;
    private final SettingsLoaderSelector providerSelector;

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
        this(root, settingFiles, new FileExtensionSettingsLoaderSelector(
                new YamlSettingsLoader(),
                new JsonSettingsLoader()
        ));
    }

    public LocalFilesSettingsSource(Path root, Iterable<Path> settingFiles, SettingsLoaderSelector providerSelector) {
        this.rootPath = root;
        this.settingFiles = settingFiles;
        this.providerSelector = providerSelector;
    }

    @Override
    public SettingsNode getSettings(Environment environment) {
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

        var settings = new DefaultSettingsNode();

        for (Path path : settingsPaths) {
            try (InputStream is = new FileInputStream(path.toFile())) {

                SettingsLoader settingsLoader = providerSelector.getProvider(path.getFileName().toString());
                settings.setAll(settingsLoader.load(is));

            } catch (FileNotFoundException e) {
                throw new EnvironmentException("Settings file doesn't exist: " + path);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to load settings file: " + path);
            }
        }

        return settings;
    }
}
