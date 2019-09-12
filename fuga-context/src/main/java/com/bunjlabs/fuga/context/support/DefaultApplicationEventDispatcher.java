package com.bunjlabs.fuga.context.support;

import com.bunjlabs.fuga.context.ApplicationEvent;
import com.bunjlabs.fuga.context.ApplicationEventDispatcher;
import com.bunjlabs.fuga.context.ApplicationListener;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.*;

public class DefaultApplicationEventDispatcher implements ApplicationEventDispatcher {

    private final Map<ListenerKey, ListenersCache> listeners = new HashMap<>();

    @Override
    public void dispatch(ApplicationEvent event) {
        var listenerKey = new ListenerKey(event.getClass());
        var listenerCache = getListenersCache(listenerKey);

        listenerCache.applicationListeners.forEach(l -> invokeListener(l, event));
    }

    @Override
    public void addEventListener(Class<? extends ApplicationEvent> eventType, ApplicationListener listener) {
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
        private final Set<ApplicationListener> applicationListeners = new LinkedHashSet<>();
    }
}
