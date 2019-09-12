package com.bunjlabs.fuga.context;

import com.bunjlabs.fuga.environment.Environment;
import com.bunjlabs.fuga.inject.Injector;

public interface ApplicationContext extends Lifecycle {

    String getId();

    String getApplicationName();

    long getStartupTime();

    Environment getEnvironment();

    Injector getInjector();
}
