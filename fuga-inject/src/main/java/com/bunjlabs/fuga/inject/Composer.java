package com.bunjlabs.fuga.inject;

public interface Composer {

    <T> T get(Class<?> requester, Class<T> required) throws ProvisionException;

}
