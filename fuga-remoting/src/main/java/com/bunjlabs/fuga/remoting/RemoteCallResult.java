package com.bunjlabs.fuga.remoting;

public interface RemoteCallResult {
    Object getValue();

    Throwable getException();

    boolean hasException();
}
