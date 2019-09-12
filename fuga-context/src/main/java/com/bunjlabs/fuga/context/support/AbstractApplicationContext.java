package com.bunjlabs.fuga.context.support;

import com.bunjlabs.fuga.context.ApplicationEventDispatcher;
import com.bunjlabs.fuga.context.ApplicationEventPublisher;
import com.bunjlabs.fuga.context.ConfigurableApplicationContext;
import com.bunjlabs.fuga.context.events.ContextClosedEvent;
import com.bunjlabs.fuga.context.events.ContextStartedEvent;
import com.bunjlabs.fuga.context.events.ContextStoppedEvent;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.concurrent.atomic.AtomicBoolean;


abstract class AbstractApplicationContext implements ConfigurableApplicationContext {

    private final Object shutdownMonitor = new Object();
    private final AtomicBoolean active = new AtomicBoolean(false);
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private String id = ObjectUtils.getIdentityHexString(this);
    private String applicationName = id;
    private long startupTime = 0;
    private ApplicationEventDispatcher applicationEventDispatcher;
    private ApplicationEventPublisher applicationEventPublisher;
    private Thread shutdownHook;

    AbstractApplicationContext() {
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

        initApplicationEventDispatcher();

        registerShutdownHook();

        this.active.set(true);

    }

    private void initApplicationEventDispatcher() {
        var injector = getInjector();
        this.applicationEventDispatcher = injector.getInstance(ApplicationEventDispatcher.class);
    }

    private ApplicationEventDispatcher getApplicationEventDispatcher() {
        if (applicationEventDispatcher == null) {
            throw new IllegalStateException("ApplicationEventDispatcher not initialized");
        }
        return applicationEventDispatcher;
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
            try {
                getApplicationEventDispatcher().dispatch(new ContextClosedEvent(this));
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        getApplicationEventDispatcher().dispatch(new ContextStartedEvent(this));
    }

    @Override
    public void stop() {
        getApplicationEventDispatcher().dispatch(new ContextStoppedEvent(this));
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
