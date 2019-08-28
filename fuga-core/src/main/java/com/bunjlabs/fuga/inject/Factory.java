package com.bunjlabs.fuga.inject;

public interface Factory {

    <T> T get(Class<T> requiredSettings) throws ProvisionException;

}
