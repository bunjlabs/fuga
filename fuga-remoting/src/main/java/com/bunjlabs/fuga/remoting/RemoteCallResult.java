package com.bunjlabs.fuga.remoting;

public class RemoteCallResult {

    private Object value;

    private Throwable exception;

    public RemoteCallResult() {
    }

    public RemoteCallResult(Object value) {
        this.value = value;
    }

    public RemoteCallResult(Throwable exception) {
        this.exception = exception;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public boolean hasException() {
        return this.exception != null;
    }

    @Override
    public String toString() {
        if (hasException()) {
            return "RemoteCallResult{" +
                    "exception=" + exception +
                    '}';
        } else {
            return "RemoteCallResult{" +
                    "value=" + value +
                    '}';
        }

    }
}
