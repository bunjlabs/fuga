package com.bunjlabs.fuga.remoting.support;

public class DefaultRemoteCallResult implements com.bunjlabs.fuga.remoting.RemoteCallResult {

    private final Object value;
    private final Throwable exception;

    public DefaultRemoteCallResult(Object value) {
        this.value = value;
        this.exception = null;
    }

    public DefaultRemoteCallResult(Throwable exception) {
        this.value = null;
        this.exception = exception;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Throwable getException() {
        return exception;
    }

    @Override
    public boolean hasException() {
        return this.exception != null;
    }

    @Override
    public String toString() {
        if (hasException()) {
            return "DefaultRemoteCallResult{" +
                    "exception=" + exception +
                    '}';
        } else {
            return "DefaultRemoteCallResult{" +
                    "value=" + value +
                    '}';
        }

    }
}
