package com.bunjlabs.fuga.inject;

public interface Composer {

    <T> T get(Class<T> requiredSettings) throws ProvisionException;

}
