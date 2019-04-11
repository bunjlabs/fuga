package com.bunjlabs.fuga.examples.minimal;

import com.bunjlabs.fuga.app.Configuration;
import com.bunjlabs.fuga.app.Configurator;
import com.bunjlabs.fuga.remoting.RemoteAccessor;
import com.bunjlabs.fuga.remoting.RemoteCallResult;
import com.bunjlabs.fuga.remoting.RemoteExecutor;
import com.bunjlabs.fuga.transport.TransportException;

public class AppConfigurator implements Configurator {

    @Override
    public void configure(Configuration configuration) throws TransportException {
        RemoteAccessor remoteAccessor = new RemoteAccessor();
        remoteAccessor.setServiceInterface(TestRemoteService.class);

        RemoteExecutor remoteExecutor = call -> new RemoteCallResult("test call result");

        TestRemoteService serviceProxy = remoteAccessor.getServiceProxy(remoteExecutor, TestRemoteService.class);

        configuration.add(TestRemoteService.class, serviceProxy);

        //Transport httpTransport = new HttpServerTransport();
        //httpTransport.enable();

        //configuration.add(httpTransport);
        configuration.add(TestInterfaceImpl1.class);

        configuration.add(TestServiceImpl.class);
    }
}
