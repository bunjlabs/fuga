package com.bunjlabs.fuga.context;

import com.bunjlabs.fuga.ioc.IocContainer;
import com.bunjlabs.fuga.ioc.Unit;
import com.bunjlabs.fuga.ioc.support.DefaultIocContainer;

public class StaticApplicationContext implements ConfigurableApplicationContext {

    private final DefaultIocContainer container = new DefaultIocContainer();

    @Override
    public void insertModule(Class<? extends Unit> unitClass) {
        try {
            container.register(unitClass);
            container.configureUnit(container.getService(unitClass));
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to configure unit: " + unitClass, e);
        }
    }

    @Override
    public void insertModule(Unit unit) {
        try {
            container.configureUnit(unit);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to configure unit: " + unit.getClass(), e);
        }
    }


    @Override
    public IocContainer getIocContainer() {
        return container;
    }
}
