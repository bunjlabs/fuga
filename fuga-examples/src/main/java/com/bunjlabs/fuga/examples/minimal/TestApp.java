/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.examples.minimal;

import com.bunjlabs.fuga.app.StaticApplicationContext;
import com.bunjlabs.fuga.dependency.support.DefaultIocContainer;
import com.bunjlabs.fuga.dependency.IocContainer;
import com.bunjlabs.fuga.settings.DefaultSettingsProvider;
import com.bunjlabs.fuga.settings.SettingsProvider;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TestApp {

    public static void main(String[] args) throws Exception {
        StaticApplicationContext applicationContext = new StaticApplicationContext();

        applicationContext.runConfigurator(AppConfigurator.class);

        TestService testService = applicationContext.getIocContainer().getService(TestServiceImpl.class);

        System.out.println(testService.test());

        SettingsProvider settingsProvider = new DefaultSettingsProvider();

        FirstHttpSettings httpSettings = settingsProvider.provide(FirstHttpSettings.class);

        System.out.format("%s - %s:%d\n", httpSettings.name(), httpSettings.host(), 8080);


    }

    public void prepare() throws Exception {
        IocContainer container = new DefaultIocContainer();

        container.register(TestInterfaceImpl1.class);
        container.register(TestServiceImpl.class);


        TestService testService = container.getService(TestServiceImpl.class);

        testService.test();

        /*
        RemoteAccessor remoteAccessor = new RemoteAccessor();
        remoteAccessor.setServiceInterface(TestService.class);

        RemoteExecutor remoteExecutor = call -> new RemoteCallResult("test call result");

        Dependency serviceProxy = remoteAccessor.getServiceProxy(remoteExecutor);

        dependencyManager.add(serviceProxy.getTargetClass(), serviceProxy.getTargetInstance());

        TestService service = (TestService) dependencyManager.getDependency(TestService.class).getTargetInstance();

        System.out.println(service.test());


        dependencyManager.registerAndInject(TestInterfaceImpl1.class);
        dependencyManager.registerAndInject(TestInterfaceImpl2.class);
        dependencyManager.registerAndInject(TestServiceImpl.class);
        RpcService rpcService = dependencyManager.registerAndInject(RpcService.class);

        Transport httptransport = new HttpServerTransport();

        ChannelAdapter rpcAdapter = new GsonRpcAdapter();
        httptransport.registerChannelHandler(rpcAdapter);

        rpcAdapter.registerChannelHandler(rpcService);

        httptransport.enable();
        */
    }

}
