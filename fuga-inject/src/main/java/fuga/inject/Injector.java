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

import fuga.common.Key;

import java.util.Set;

public interface Injector {

    static Injector create(Unit... units) {
        return new InjectorBuilder().withUnits(units).build();
    }

    static Injector create(Iterable<Unit> units) {
        return new InjectorBuilder().withUnits(units).build();
    }

    Injector getParent();

    Injector createChildInjector(Unit... units);

    Injector createChildInjector(Iterable<Unit> units);

    <T> Binding<T> getBinding(Class<T> type);

    <T> Binding<T> getBinding(Key<T> key);

    <T> Set<Binding<T>> getAllBindings(Class<T> type);

    <T> Set<Binding<T>> getAllBindings(Key<T> key);

    <T> Provider<T> getProvider(Class<T> type);

    <T> Provider<T> getProvider(Key<T> key);

    <T> Set<Provider<T>> getAllProviders(Class<T> type);

    <T> Set<Provider<T>> getAllProviders(Key<T> key);

    <T> T getInstance(Class<T> type);

    <T> T getInstance(Key<T> key);

    <T> Set<T> getAllInstances(Class<T> type);

    <T> Set<T> getAllInstances(Key<T> key);
}
