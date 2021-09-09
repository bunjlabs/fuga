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

package fuga.inject;

import fuga.inject.support.InternalInjectorBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InjectorBuilder {

    private final List<Unit> units = new LinkedList<>();

    public InjectorBuilder withUnits(Unit... units) {
        this.units.addAll(Arrays.asList(units));
        return this;
    }

    public InjectorBuilder withUnits(Iterable<Unit> units) {
        units.forEach(this.units::add);
        return this;
    }

    public Injector build() {
        return new InternalInjectorBuilder().withUnits(units).build();
    }
}
