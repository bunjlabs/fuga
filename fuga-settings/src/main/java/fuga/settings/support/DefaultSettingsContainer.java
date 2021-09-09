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

import fuga.environment.Environment;
import fuga.settings.MutableSettingsNode;
import fuga.settings.SettingsContainer;
import fuga.settings.SettingsException;
import fuga.settings.SettingsNode;
import fuga.settings.source.SettingsSource;
import fuga.util.Assert;

import java.util.LinkedList;
import java.util.List;

public class DefaultSettingsContainer implements SettingsContainer {

    private final MutableSettingsNode rootNode;
    private final List<MutableSettingsNode> persistedTrees;

    public DefaultSettingsContainer() {
        this.rootNode = new DefaultSettingsNode();
        this.persistedTrees = new LinkedList<>();
    }


    public DefaultSettingsContainer(MutableSettingsNode settingsTree) {
        this.rootNode = Assert.notNull(settingsTree);
        this.persistedTrees = new LinkedList<>();
    }

    @Override
    public void checkAvailability() throws SettingsException {
        var errorTree = new DefaultSettingsNode();

        persistedTrees.forEach(node -> checkAvailability(errorTree, node));
    }

    private void checkAvailability(MutableSettingsNode errorNode, SettingsNode node) {
        node.keys().forEach(key -> {
            if (!node.contains(key)) {
                errorNode.set(key, new DefaultSettingsValue(Object.class));
            } else if (!node.get(key).isValuePresent()) {
                errorNode.set(key, new DefaultSettingsValue(node.get(key)));
            }
        });

        node.childrenNames().forEach(child -> checkAvailability(errorNode.node(child), node.node(child)));
    }

    @Override
    public void merge(SettingsNode settingsNode) {
        rootNode.merge(Assert.notNull(settingsNode));
    }

    @Override
    public void load(SettingsSource settingsSource, Environment environment) {
        Assert.notNull(settingsSource);
        Assert.notNull(environment);

        var settings = settingsSource.getSettings(environment);

        rootNode.setAll(settings);
        updatePersistedTrees();
    }

    @Override
    public void persist(MutableSettingsNode settingTree) {
        Assert.notNull(settingTree);

        settingTree.merge(rootNode);
        persistedTrees.add(settingTree);
    }

    private void updatePersistedTrees() {
        persistedTrees.forEach(tree -> tree.merge(rootNode));
    }
}
