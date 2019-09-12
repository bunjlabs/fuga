package com.bunjlabs.fuga.context;

public interface Lifecycle {

    void start();

    void stop();

    boolean isRunning();
}
