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

package fuga.app.support;

import fuga.app.ConfigurableApplicationContext;
import fuga.app.events.ApplicationClosedEvent;
import fuga.app.events.ApplicationInitializedEvent;
import fuga.app.events.ApplicationStartedEvent;
import fuga.app.events.ApplicationStoppedEvent;
import fuga.event.EventBus;
import fuga.util.ObjectUtils;

import java.util.concurrent.atomic.AtomicBoolean;

abstract class AbstractApplicationContext implements ConfigurableApplicationContext {

    private final Object shutdownMonitor = new Object();
    private final AtomicBoolean active = new AtomicBoolean(false);
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final EventBus eventBus;

    private final String id = ObjectUtils.getIdentityHexString(this);
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
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public long getStartupTime() {
        return startupTime;
    }

    @Override
    public void init() {
        startupTime = System.currentTimeMillis();
        registerShutdownHook();
        active.set(true);
        eventBus.post(new ApplicationInitializedEvent(this));
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
        stop();

        if (active.get()) {
            eventBus.post(new ApplicationClosedEvent(this));
        }
    }

    @Override
    public void start() {
        if (!started.compareAndExchange(false, true)) {
            eventBus.post(new ApplicationStartedEvent(this));
        }
    }

    @Override
    public void stop() {
        if (started.compareAndExchange(true, false)) {
            eventBus.post(new ApplicationStoppedEvent(this));
        }
    }

    @Override
    public void close() {
        synchronized (shutdownMonitor) {
            doClose();
            if (shutdownHook != null) {
                try {
                    Runtime.getRuntime().removeShutdownHook(shutdownHook);
                } catch (IllegalStateException ex) {
                    // ignore
                }
            }
        }
    }

    @Override
    public boolean isRunning() {
        return active.get() && started.get() && !closed.get();
    }
}
