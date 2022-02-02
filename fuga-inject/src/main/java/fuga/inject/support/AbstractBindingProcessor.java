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
import fuga.common.errors.ErrorMessages;

import java.util.LinkedList;
import java.util.List;

abstract class AbstractBindingProcessor implements BindingProcessor {

    private final Container container;
    private final ErrorMessages errorMessages;
    private final List<Initializable> initializables = new LinkedList<>();

    AbstractBindingProcessor(Container container, ErrorMessages errorMessages) {
        this.container = container;
        this.errorMessages = errorMessages;
    }

    <T> List<BindingAttachment<T>> getAttachments(Key<T> key) {
        return container.getAttachments(key);
    }

    <T> List<BindingEncounter<T>> getEncounters(Key<T> key) {
        return container.getEncounters(key);
    }

    <T> List<BindingWatching<T>> getWatchings(Key<T> key) {
        return container.getWatchings(key);
    }

    void scheduleInitialization(Initializable initializable) {
        initializables.add(initializable);
    }

    void initialize(InjectorImpl injector) {
        initializables.forEach(u -> u.initialize(injector));
    }

    ErrorMessages getErrorMessages() {
        return errorMessages;
    }
}
