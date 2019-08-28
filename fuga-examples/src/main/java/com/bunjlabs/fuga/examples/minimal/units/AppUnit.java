package com.bunjlabs.fuga.examples.minimal.units;

import com.bunjlabs.fuga.examples.minimal.services.TestInterface;
import com.bunjlabs.fuga.examples.minimal.services.TestInterfaceImpl;
import com.bunjlabs.fuga.examples.minimal.services.TestService;
import com.bunjlabs.fuga.examples.minimal.services.TestServiceImpl;
import com.bunjlabs.fuga.inject.Configuration;
import com.bunjlabs.fuga.inject.Unit;

public class AppUnit implements Unit {
    @Override
    public void configure(Configuration c) {
        c.bind(TestService.class).to(TestServiceImpl.class);
        c.bind(TestInterface.class).to(TestInterfaceImpl.class);

        c.install(new HttpServerUnit());
    }
}
