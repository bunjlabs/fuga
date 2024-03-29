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

import java.util.List;
import java.util.function.Consumer;

interface Container {

    Container EMPTY = new EmptyContainer();

    <T> AbstractBinding<T> getExplicitBinding(Key<T> key);

    <T> List<AbstractBinding<T>> getAllExplicitBindings(Key<T> key);

    <T> List<BindingAttachment<T>> getAttachments(Key<T> key);

    <T> List<BindingEncounter<T>> getEncounters(Key<T> key);

    <T> List<BindingWatching<T>> getWatchings(Key<T> key);

    void putBinding(AbstractBinding<?> binding);

    void putAttachment(BindingAttachment<?> attachment);

    void putEncounter(BindingEncounter<?> encounter);

    void putWatching(BindingWatching<?> watching);

    void forEach(Consumer<? extends AbstractBinding<?>> action);
}
