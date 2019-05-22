package com.bunjlabs.fuga.remoting;

import com.bunjlabs.fuga.ioc.Module;
import com.bunjlabs.fuga.ioc.ModuleBuilder;
import com.bunjlabs.fuga.remoting.support.DefaultRemoteAccessor;
import com.bunjlabs.fuga.remoting.support.DefaultRemoteCallResult;
import com.bunjlabs.fuga.util.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RemoteAccessorModuleBuilder implements ModuleBuilder {

    private RemoteExecutor remoteExecutor;
    private Set<Class<?>> interfacesSet;

    public RemoteAccessorModuleBuilder() {
        this.remoteExecutor = (c) -> new DefaultRemoteCallResult(new UnsupportedOperationException());
        this.interfacesSet = new HashSet<>();
    }

    public RemoteAccessorModuleBuilder withRemoteExecutor(RemoteExecutor remoteExecutor) {
        this.remoteExecutor = remoteExecutor;
        return this;
    }

    public RemoteAccessorModuleBuilder withInterfaces(Class<?>... interfaces) {
        for (Class<?> i : interfaces) Assert.isTrue(i.isInterface(), "all input classes must be an interfaces");
        interfacesSet.addAll(Arrays.asList(interfaces));
        return this;
    }

    @Override
    public Module build() {
        return configuration -> {
            RemoteAccessor remoteAccessor = new DefaultRemoteAccessor();

            interfacesSet.forEach(i -> configuration.add(i, remoteAccessor.getServiceProxy(remoteExecutor, i)));
        };
    }
}
