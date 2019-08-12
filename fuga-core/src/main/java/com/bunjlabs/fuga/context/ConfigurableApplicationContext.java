package com.bunjlabs.fuga.context;

import com.bunjlabs.fuga.ioc.Unit;

public interface ConfigurableApplicationContext extends ApplicationContext {

    void insertModule(Class<? extends Unit> moduleClass);

    void insertModule(Unit unit);
}
