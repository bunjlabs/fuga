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
import fuga.lang.TypeLiteral;
import fuga.util.Matcher;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class InheritedContainer implements Container {

    private final Map<Key<?>, List<AbstractBinding<?>>> explicitBindingsMutable = new LinkedHashMap<>();
    private final Map<Key<?>, List<AbstractBinding<?>>> explicitBindings = Collections.unmodifiableMap(explicitBindingsMutable);
    private final List<BindingAttachment<?>> attachments = new ArrayList<>();
    private final List<BindingEncounter<?>> encounters = new ArrayList<>();
    private final List<BindingWatching<?>> watchings = new ArrayList<>();
    private final Container parent;

    InheritedContainer(Container parent) {
        this.parent = parent;
    }

    @Override
    public <T> AbstractBinding<T> getExplicitBinding(Key<T> key) {
        var binding = doGetBinding(key);
        return binding != null ? binding : parent.getExplicitBinding(key);
    }

    @Override
    public <T> List<AbstractBinding<T>> getAllExplicitBindings(Key<T> key) {
        var localBindings = doGetAllBindings(key);
        return Stream.of(parent.getAllExplicitBindings(key), localBindings)
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public <T> List<BindingAttachment<T>> getAttachments(Key<T> key) {
        var streamA = parent.getAttachments(key).stream();

        var streamB = attachments.stream().filter(a -> {
            @SuppressWarnings("unchecked")
            var matcher = (Matcher<? super TypeLiteral<?>>) a.getMatcher();

            return matcher.match(key.getType());
        }).map(a -> {
            @SuppressWarnings("unchecked")
            var cast = (BindingAttachment<T>) a;
            return cast;
        });

        return Stream.concat(streamA, streamB).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public <T> List<BindingEncounter<T>> getEncounters(Key<T> key) {
        var streamA = parent.getEncounters(key).stream();

        var streamB = encounters.stream().filter(a -> {
            @SuppressWarnings("unchecked")
            var matcher = (Matcher<? super TypeLiteral<?>>) a.getMatcher();

            return matcher.match(key.getType());
        }).map(a -> {
            @SuppressWarnings("unchecked")
            var cast = (BindingEncounter<T>) a;
            return cast;
        });

        return Stream.concat(streamA, streamB).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public <T> List<BindingWatching<T>> getWatchings(Key<T> key) {
        var streamA = parent.getWatchings(key).stream();

        var streamB = watchings.stream().filter(a -> {
            @SuppressWarnings("unchecked")
            var matcher = (Matcher<? super TypeLiteral<?>>) a.getMatcher();

            return matcher.match(key.getType());
        }).map(a -> {
            @SuppressWarnings("unchecked")
            var cast = (BindingWatching<T>) a;
            return cast;
        });

        return Stream.concat(streamA, streamB).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void putBinding(AbstractBinding<?> binding) {
        doPutBinding(binding);
    }

    @Override
    public void putAttachment(BindingAttachment<?> attachment) {
        attachments.add(attachment);
    }

    @Override
    public void putEncounter(BindingEncounter<?> encounter) {
        encounters.add(encounter);
    }

    @Override
    public void putWatching(BindingWatching<?> watching) {
        watchings.add(watching);
    }

    @Override
    public void forEach(Consumer<? extends AbstractBinding<?>> action) {
        @SuppressWarnings("unchecked")
        var castedAction = (Consumer<? super AbstractBinding<?>>) action;

        explicitBindings.values().forEach(bindings -> bindings.forEach(castedAction));
        parent.forEach(action);
    }

    @SuppressWarnings("unchecked")
    private <T> AbstractBinding<T> doGetBinding(Key<T> key) {
        var bindings = explicitBindings.get(key);
        // key is obtained from the binding, so that key and binding inner types will always be the same
        return bindings != null && !bindings.isEmpty() ? (AbstractBinding<T>) bindings.get(bindings.size() - 1) : null;
    }

    @SuppressWarnings("unchecked")
    private <T> List<AbstractBinding<T>> doGetAllBindings(Key<T> key) {
        var bindings = explicitBindings.get(key);

        if (bindings == null) {
            return Collections.emptyList();
        }

        var localBindings = new ArrayList<AbstractBinding<T>>(bindings.size());
        for (var binding : bindings) {
            // key is obtained from the binding, so that key and binding inner types will always be the same
            localBindings.add((AbstractBinding<T>) binding);
        }


        return Collections.unmodifiableList(localBindings);
    }

    private void doPutBinding(AbstractBinding<?> binding) {
        var key = binding.getKey();

        List<AbstractBinding<?>> bindings;
        if (explicitBindingsMutable.containsKey(key)) {
            bindings = explicitBindingsMutable.get(key);
        } else {
            bindings = new ArrayList<>();
            explicitBindingsMutable.put(key, bindings);
        }

        bindings.add(binding);
    }
}
