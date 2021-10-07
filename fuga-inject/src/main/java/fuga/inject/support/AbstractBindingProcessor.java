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

import fuga.common.errors.ErrorMessages;
import fuga.common.Key;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

abstract class AbstractBindingProcessor implements BindingProcessor {

    private final Container container;
    private final ErrorMessages errorMessages;
    private final List<Initializable> uninitialized = new LinkedList<>();

    AbstractBindingProcessor(Container container, ErrorMessages errorMessages) {
        this.container = container;
        this.errorMessages = errorMessages;
    }

    ScopeBinding getScopeBinding(Class<? extends Annotation> annotationType) {
        return container.getScopeBinding(annotationType);
    }

    <T> List<AbstractKeyedWatching<T>> getKeyedWatchings(Key<T> key) {
        return container.getKeyedWatchings(key);
    }

    List<AbstractMatchedWatching> getMatchedWatchings(Key<?> key) {
        return container.getMatchedWatchings(key);
    }

    <T> void putBinding(AbstractBinding<T> binding) {
        container.putBinding(binding);
    }

    void scheduleInitialization(Initializable initializable) {
        uninitialized.add(initializable);
    }

    List<Initializable> getUninitialized() {
        return uninitialized;
    }

    ErrorMessages getErrorMessages() {
        return errorMessages;
    }
}
