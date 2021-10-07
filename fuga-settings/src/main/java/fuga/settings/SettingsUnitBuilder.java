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

package fuga.settings;

import fuga.environment.Environment;
import fuga.inject.Singleton;
import fuga.inject.Unit;
import fuga.inject.UnitBuilder;
import fuga.settings.source.SettingsSource;
import fuga.settings.support.DefaultSettingsComposer;
import fuga.settings.support.DefaultSettingsContainer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SettingsUnitBuilder implements UnitBuilder {

    private final List<SettingsSource> settingsSourcesSet = new LinkedList<>();

    private Environment environment;
    private MutableSettingsNode settingsTree;

    public SettingsUnitBuilder() {
        this.environment = Environment.DEFAULT;
    }

    public SettingsUnitBuilder withEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public SettingsUnitBuilder withSettingsTree(MutableSettingsNode settingsTree) {
        this.settingsTree = settingsTree;
        return this;
    }

    public SettingsUnitBuilder withSettingsSources(SettingsSource... settingsSources) {
        settingsSourcesSet.addAll(Arrays.asList(settingsSources));
        return this;
    }

    @Override
    public Unit build() {
        return c -> {
            if (settingsTree != null) {
                c.bind(SettingsContainer.class).toProvider(() -> new DefaultSettingsContainer(settingsTree));
            } else {
                c.bind(SettingsContainer.class).toProvider(DefaultSettingsContainer::new);
            }

            c.watch(SettingsContainer.class).with((key, container) ->
                    settingsSourcesSet.forEach(source -> container.load(source, environment)));

            c.bind(DefaultSettingsComposer.class).in(Singleton.class);
            c.bind(SettingsComposer.class).to(DefaultSettingsComposer.class);
        };
    }
}
