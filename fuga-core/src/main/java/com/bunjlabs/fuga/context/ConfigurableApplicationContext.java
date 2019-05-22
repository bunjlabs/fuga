package com.bunjlabs.fuga.context;

import com.bunjlabs.fuga.ioc.Module;

public interface ConfigurableApplicationContext extends ApplicationContext {

    void insertModule(Class<? extends Module> moduleClass);

    void insertModule(Module module);
}
