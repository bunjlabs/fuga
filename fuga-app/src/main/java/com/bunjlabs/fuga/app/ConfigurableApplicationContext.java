package com.bunjlabs.fuga.app;

public interface ConfigurableApplicationContext extends ApplicationContext {

    void runConfigurator(Class<? extends Configurator> configuratorClass);
}
