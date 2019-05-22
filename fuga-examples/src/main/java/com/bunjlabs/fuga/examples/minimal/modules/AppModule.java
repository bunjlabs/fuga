package com.bunjlabs.fuga.examples.minimal.modules;

import com.bunjlabs.fuga.examples.minimal.services.TestInterface;
import com.bunjlabs.fuga.examples.minimal.services.TestInterfaceImpl;
import com.bunjlabs.fuga.examples.minimal.services.TestService;
import com.bunjlabs.fuga.examples.minimal.services.TestServiceImpl;
import com.bunjlabs.fuga.ioc.Configuration;
import com.bunjlabs.fuga.ioc.Module;

public class AppModule implements Module {

    @Override
    public void configure(Configuration configuration) {
        configuration.add(TestService.class, TestServiceImpl.class);
        configuration.add(TestInterface.class, TestInterfaceImpl.class);
    }
}
