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
import fuga.inject.Binder;
import fuga.inject.builder.BindingBuilder;
import fuga.inject.builder.MatchBuilder;
import fuga.lang.TypeLiteral;
import fuga.util.Matcher;

import java.util.ArrayList;
import java.util.List;

class DefaultBinder implements Binder {

    private final List<AbstractBinding<?>> bindings = new ArrayList<>();
    private final List<BindingAttachment<?>> attachments = new ArrayList<>();
    private final List<BindingEncounter<?>> encounters = new ArrayList<>();
    private final List<BindingWatching<?>> watchings = new ArrayList<>();

    DefaultBinder() {
    }

    @Override
    public <T> MatchBuilder<T> match(Matcher<? super TypeLiteral<T>> matcher) {
        return new DefaultMatchBuilder<>(matcher, attachments, encounters, watchings);
    }


    @Override
    public <T> BindingBuilder<T> bind(Key<T> type) {
        return new DefaultBindingBuilder<>(type, bindings);
    }

    List<AbstractBinding<?>> getBindings() {
        return bindings;
    }

    List<BindingAttachment<?>> getAttachments() {
        return attachments;
    }

    List<BindingEncounter<?>> getEncounters() {
        return encounters;
    }

    List<BindingWatching<?>> getWatchings() {
        return watchings;
    }
}
