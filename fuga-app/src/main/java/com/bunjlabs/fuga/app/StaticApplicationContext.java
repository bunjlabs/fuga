package com.bunjlabs.fuga.app;

import com.bunjlabs.fuga.dependency.IocContainer;
import com.bunjlabs.fuga.dependency.support.DefaultIocContainer;

public class StaticApplicationContext implements ConfigurableApplicationContext {

    private final IocContainer container = new DefaultIocContainer();

    @Override
    public void runConfigurator(Class<? extends Configurator> configuratorClass) {
        try {
            container.register(configuratorClass);
            Configurator configurator = container.getService(configuratorClass);

            execConfigurator(configurator);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to run configurator", e);
        }
    }

    @Override
    public void runConfigurator(Configurator configurator) {
        try {
            container.register(configurator);

            execConfigurator(configurator);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to run configurator", e);
        }
    }


    @Override
    public IocContainer getIocContainer() {
        return container;
    }

    private void execConfigurator(Configurator configurator) throws Exception {
        DefaultConfiguration configuration = new DefaultConfiguration();

        configurator.configure(configuration);

        configuration.getObjectRegisterList().forEach(d -> {
            if (d.isTypeDefined()) {
                container.register(d.getType(), d.getInstance());
            } else {
                container.register(d.getInstance());
            }
        });
        
        configuration.getClassRegisterList().forEach(container::register);
    }
}
