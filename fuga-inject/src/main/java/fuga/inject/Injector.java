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

import java.util.Arrays;
import java.util.Set;

public interface Injector {

    static Injector create(Unit... units) {
        return new InjectorBuilder().withUnits(units).build();
    }

    static Injector create(Iterable<Unit> units) {
        return new InjectorBuilder().withUnits(units).build();
    }

    Injector getParent();

    default Injector createChildInjector(Unit... units) {
        return createChildInjector(Arrays.asList(units));
    }

    Injector createChildInjector(Iterable<? extends Unit> units);

    default <T> Binding<T> getBinding(Class<T> type) {
        return getBinding(Key.of(type));
    }

    <T> Binding<T> getBinding(Key<T> key);

    default <T> Set<Binding<T>> getAllBindings(Class<T> type) {
        return getAllBindings(Key.of(type));
    }

    <T> Set<Binding<T>> getAllBindings(Key<T> key);

    default <T> Provider<T> getProvider(Class<T> type) {
        return getProvider(Key.of(type));
    }

    <T> Provider<T> getProvider(Key<T> key);

    default <T> Set<Provider<T>> getAllProviders(Class<T> type) {
        return getAllProviders(Key.of(type));
    }

    <T> Set<Provider<T>> getAllProviders(Key<T> key);

    default <T> T getInstance(Class<T> type) {
        return getInstance(Key.of(type));
    }

    <T> T getInstance(Key<T> key);

    default <T> Set<T> getAllInstances(Class<T> type) {
        return getAllInstances(Key.of(type));
    }

    <T> Set<T> getAllInstances(Key<T> key);
}
