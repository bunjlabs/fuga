package com.bunjlabs.fuga.context;

import com.bunjlabs.fuga.ioc.IocContainer;
import com.bunjlabs.fuga.ioc.Module;
import com.bunjlabs.fuga.ioc.support.DefaultIocContainer;

public class StaticApplicationContext implements ConfigurableApplicationContext {

    private final DefaultIocContainer container = new DefaultIocContainer();

    @Override
    public void insertModule(Class<? extends Module> moduleClass) {
        try {
            container.register(moduleClass);
            Module module = container.getService(moduleClass);

            container.configureModule(module);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to configure module: " + moduleClass, e);
        }
    }

    @Override
    public void insertModule(Module module) {
        try {
            container.configureModule(module);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to configure module: " + module.getClass(), e);
        }
    }


    @Override
    public IocContainer getIocContainer() {
        return container;
    }
}
