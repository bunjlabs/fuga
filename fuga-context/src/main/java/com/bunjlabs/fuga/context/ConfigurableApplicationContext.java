package com.bunjlabs.fuga.context;

public interface ConfigurableApplicationContext extends ApplicationContext {

    void setId(String id);

    void init();

    void close();
}
