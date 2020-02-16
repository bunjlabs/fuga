/*
 * Copyright 2019-2020 Bunjlabs
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
import com.bunjlabs.fuga.context.ApplicationListener;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.*;

public class DefaultApplicationEventDispatcher implements ApplicationEventDispatcher {

    private final Map<ListenerKey, ListenersCache> listeners = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void dispatch(ApplicationEvent event) {
        var listenerKey = new ListenerKey(event.getClass());
        var listenerCache = getListenersCache(listenerKey);

        listenerCache.applicationListeners.forEach(l -> invokeListener((ApplicationListener<ApplicationEvent>) l, event));
    }

    @Override
    public void addEventListener(Class<? extends ApplicationEvent> eventType, ApplicationListener<?> listener) {
        var listenerKey = new ListenerKey(eventType);
        var listenerCache = getListenersCache(listenerKey);

        listenerCache.applicationListeners.add(listener);

    }

    private ListenersCache getListenersCache(ListenerKey listenerKey) {
        var listenersCache = listeners.get(listenerKey);
        if (listenersCache == null) {
            listeners.put(listenerKey, listenersCache = new ListenersCache());
        }
        return listenersCache;
    }

    private void invokeListener(ApplicationListener<ApplicationEvent> listener, ApplicationEvent event) {
        try {
            listener.onApplicationEvent(event);
        } catch (ClassCastException ex) {
            // TODO logging
            ex.printStackTrace();
        }
    }

    private static class ListenerKey {
        private final Class<? extends ApplicationEvent> eventType;

        ListenerKey(Class<? extends ApplicationEvent> eventType) {
            this.eventType = eventType;
        }

        @Override
        public String toString() {
            return ObjectUtils.toStringJoiner(this)
                    .add("eventType", eventType)
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ListenerKey) {
                ListenerKey other = (ListenerKey) o;
                return eventType.equals(other.eventType);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(eventType);
        }
    }

    private static class ListenersCache {
        private final Set<ApplicationListener<? extends ApplicationEvent>> applicationListeners = new LinkedHashSet<>();
    }
}
