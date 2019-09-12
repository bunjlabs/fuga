package com.bunjlabs.fuga.context.support;

import com.bunjlabs.fuga.environment.Environment;
import com.bunjlabs.fuga.inject.Injector;

public class StaticApplicationContext extends AbstractApplicationContext {
    private final Environment environment;
    private Injector injector;

    public StaticApplicationContext(Environment environment) {
        super();
        this.environment = environment;
    }

    @Override
    public Injector getInjector() {
        return injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

}
