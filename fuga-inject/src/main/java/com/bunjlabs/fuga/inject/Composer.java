package com.bunjlabs.fuga.inject;

public interface Composer {

    <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException;
}
