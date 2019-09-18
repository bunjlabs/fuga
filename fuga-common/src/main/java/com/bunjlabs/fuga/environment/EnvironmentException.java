package com.bunjlabs.fuga.environment;

public class EnvironmentException extends RuntimeException {
    public EnvironmentException(String msg) {
        super(msg);
    }

    public EnvironmentException(String msg, Throwable cause) {
        super(msg, cause);
    }
}