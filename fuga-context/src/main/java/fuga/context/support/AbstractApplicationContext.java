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

package fuga.context.support;

import fuga.context.ConfigurableApplicationContext;
import fuga.context.events.ApplicationClosedEvent;
import fuga.context.events.ApplicationStartedEvent;
import fuga.context.events.ApplicationStoppedEvent;
import fuga.event.EventBus;
import fuga.util.ObjectUtils;

import java.util.concurrent.atomic.AtomicBoolean;

abstract class AbstractApplicationContext implements ConfigurableApplicationContext {

    private final Object shutdownMonitor = new Object();
    private final AtomicBoolean active = new AtomicBoolean(false);
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final EventBus eventBus;

    private String id = ObjectUtils.getIdentityHexString(this);
    private final String applicationName = id;

    private long startupTime = 0;
    private Thread shutdownHook;

    AbstractApplicationContext(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public long getStartupTime() {
        return startupTime;
    }

    @Override
    public void init() {
        this.startupTime = System.currentTimeMillis();

        registerShutdownHook();

        this.active.set(true);
    }

    private void registerShutdownHook() {
        if (shutdownHook == null) {
            shutdownHook = new Thread(() -> {
                synchronized (shutdownMonitor) {
                    doClose();
                }
            });
            Runtime.getRuntime().addShutdownHook(shutdownHook);
        }
    }

    private void doClose() {
        if (this.active.get()) {
            eventBus.fire(new ApplicationClosedEvent(this));
        }
    }

    @Override
    public void start() {
        eventBus.fire(new ApplicationStartedEvent(this));
    }

    @Override
    public void stop() {
        eventBus.fire(new ApplicationStoppedEvent(this));
    }

    @Override
    public void close() {
        synchronized (this.shutdownMonitor) {
            doClose();
            if (shutdownHook != null) {
                try {
                    Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
                } catch (IllegalStateException ex) {
                    // ignore
                }
            }
        }
    }

    @Override
    public boolean isRunning() {
        return active.get() && !closed.get();
    }
}
