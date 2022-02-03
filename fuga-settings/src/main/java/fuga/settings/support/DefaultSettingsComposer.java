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

import fuga.common.Key;
import fuga.inject.Inject;
import fuga.inject.InjectHint;
import fuga.settings.SettingsComposer;
import fuga.settings.SettingsException;

public class DefaultSettingsComposer implements SettingsComposer {

    private final SettingsAgent agent;

    @Inject
    public DefaultSettingsComposer(@InjectHint(target = InjectHint.Target.ATTRIBUTE) SettingsAgent agent) {
        this.agent = agent;
    }

    @Override
    public <T> T get(Key<?> requester, Key<T> requested) throws SettingsException {
        if (!requested.getType().isAssignableFrom(agent.getProxiedClass())) {
            throw new SettingsException(String.format(
                    "Requested type %s does not assignable from configured type %s",
                    requested.getType(), agent.getProxiedClass()));
        }

        return getCastedProxy();
    }

    @SuppressWarnings("unchecked")
    private <T> T getCastedProxy() {
        return (T) agent.getProxyObject();
    }
}