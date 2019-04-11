package com.bunjlabs.fuga.app;

import com.bunjlabs.fuga.dependency.DefaultIocContainer;
import com.bunjlabs.fuga.dependency.IocContainer;

public class StaticApplicationContext implements ConfigurableApplicationContext {

    private final IocContainer container = new DefaultIocContainer();

    @Override
    public void runConfigurator(Class<? extends Configurator> configuratorClass) {
        try {
            DefaultConfiguration configuration = new DefaultConfiguration();

            container.register(configuratorClass);

            Configurator configurator = container.getService(configuratorClass);
            configurator.configure(configuration);

            configuration.getObjectRegisterList().forEach(d -> container.register(d.getType(), d.getInstance()));
            configuration.getClassRegisterList().forEach(container::register);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to run configurator", e);
        }
    }


    @Override
    public IocContainer getIocContainer() {
        return container;
    }
}
