package com.bunjlabs.fuga.remoting;

import com.bunjlabs.fuga.inject.Unit;
import com.bunjlabs.fuga.inject.UnitBuilder;
import com.bunjlabs.fuga.remoting.support.DefaultRemoteAccessor;
import com.bunjlabs.fuga.remoting.support.DefaultRemoteCallResult;
import com.bunjlabs.fuga.util.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RemoteAccessorUnitBuilder implements UnitBuilder {

    private RemoteExecutor remoteExecutor;
    private Set<Class<?>> interfacesSet;

    public RemoteAccessorUnitBuilder() {
        this.remoteExecutor = (c) -> new DefaultRemoteCallResult(new UnsupportedOperationException());
        this.interfacesSet = new HashSet<>();
    }

    public RemoteAccessorUnitBuilder withRemoteExecutor(RemoteExecutor remoteExecutor) {
        this.remoteExecutor = remoteExecutor;
        return this;
    }

    public RemoteAccessorUnitBuilder withInterfaces(Class<?>... interfaces) {
        for (Class<?> i : interfaces) Assert.isTrue(i.isInterface(), "all input classes must be an interfaces");
        interfacesSet.addAll(Arrays.asList(interfaces));
        return this;
    }

    @Override
    public Unit build() {
        return configuration -> {
            RemoteAccessor remoteAccessor = new DefaultRemoteAccessor();

            //interfacesSet.forEach(i -> configuration.bind(i, remoteAccessor.getServiceProxy(remoteExecutor, i)));
        };
    }
}
