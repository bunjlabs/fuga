package com.bunjlabs.fuga.context;

import com.bunjlabs.fuga.inject.Injector;
import com.bunjlabs.fuga.inject.Unit;
import com.bunjlabs.fuga.inject.support.InjectorBuilder;


public class ApplicationContext {
    private final Injector injector;

    public ApplicationContext(Injector injector) {
        this.injector = injector;
    }

    public static ApplicationContext fromUnits(Unit... units) {
        Injector injector = new InjectorBuilder()
                .withUnits(units)
                .build();

        return new ApplicationContext(injector);
    }

    public Injector getInjector() {
        return injector;
    }
}
