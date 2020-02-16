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

package com.bunjlabs.fuga.settings.support;

import com.bunjlabs.fuga.settings.SettingsNode;
import com.bunjlabs.fuga.settings.SettingsValue;

import java.util.Collections;
import java.util.Set;

public final class EmptySettingsNode implements SettingsNode {

    public static final EmptySettingsNode INSTANCE = new EmptySettingsNode();

    EmptySettingsNode() {
    }

    @Override
    public SettingsNode node(String name) {
        return this;
    }

    @Override
    public boolean nodeExists(String name) {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> childrenNames() {
        return Collections.EMPTY_SET;
    }

    @Override
    public SettingsValue get(String key) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> keys() {
        return Collections.EMPTY_SET;
    }

    @Override
    public boolean contains(String key) {
        return false;
    }
}
