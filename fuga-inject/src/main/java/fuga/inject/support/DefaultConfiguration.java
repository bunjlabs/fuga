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

package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.Configuration;
import fuga.inject.Unit;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

class DefaultConfiguration extends DefaultBinder implements Configuration {

    private final Set<Key<? extends Unit>> requiredUnits = new HashSet<>();
    private final List<Unit> installedUnits = new LinkedList<>();

    DefaultConfiguration() {
        super();
    }

    Set<Key<? extends Unit>> getRequiredUnits() {
        return requiredUnits;
    }

    List<Unit> getInstalledUnits() {
        return installedUnits;
    }

    @Override
    public void depends(Key<? extends Unit> unitType) {
        requiredUnits.add(unitType);
    }

    @Override
    public void install(Unit unit) {
        installedUnits.add(unit);
    }

}
