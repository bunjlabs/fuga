package com.bunjlabs.fuga.context;

import com.bunjlabs.fuga.inject.Injector;
import com.bunjlabs.fuga.inject.Unit;
import com.bunjlabs.fuga.inject.support.InjectorBuilder;


public class ApplicationContext {
    private final Injector injector;

    public ApplicationContext(Injector injector) {
        this.injector = injector;
    }

    public static ApplicationContext fromUnit(Unit unit) {
        Injector injector = new InjectorBuilder()
                .withUnits(unit)
                .build();

        return new ApplicationContext(injector);
    }

    public Injector getInjector() {
        return injector;
    }
}
