package com.bunjlabs.fuga.remoting;

public interface RemoteAccessor {
    <T> T getServiceProxy(RemoteExecutor executor, Class<T> serviceInterface);
}
