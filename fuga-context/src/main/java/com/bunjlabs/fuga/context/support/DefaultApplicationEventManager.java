/*
 * Copyright 2019 Bunjlabs
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

package com.bunjlabs.fuga.context.support;

import com.bunjlabs.fuga.context.ApplicationEvent;
import com.bunjlabs.fuga.context.ApplicationEventDispatcher;
import com.bunjlabs.fuga.context.ApplicationEventManager;
import com.bunjlabs.fuga.context.ApplicationListener;
import com.bunjlabs.fuga.util.FullType;

public class DefaultApplicationEventManager implements ApplicationEventManager {

    private final ApplicationEventDispatcher eventDispatcher;

    public DefaultApplicationEventManager(ApplicationEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public <T extends ApplicationEvent> void addEventListener(ApplicationListener<T> listener) {
        var genericType = FullType.of(listener.getClass()).as(ApplicationListener.class).getGeneric(0);
        if (genericType == FullType.EMPTY || !ApplicationEvent.class.isAssignableFrom(genericType.getRawType())) {
            throw new IllegalArgumentException();
        }

        @SuppressWarnings("unchecked")
        var eventType = (FullType<? extends ApplicationEvent>) genericType;

        eventDispatcher.addEventListener(eventType.getRawType(), listener);
    }
}
