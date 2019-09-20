package com.bunjlabs.fuga.context;

import com.bunjlabs.fuga.context.support.DefaultApplicationEventDispatcher;
import com.bunjlabs.fuga.context.support.DefaultApplicationEventManager;
import com.bunjlabs.fuga.context.support.DefaultApplicationEventPublisher;
import com.bunjlabs.fuga.context.support.StaticApplicationContext;
import com.bunjlabs.fuga.environment.Environment;
import com.bunjlabs.fuga.inject.Unit;
import com.bunjlabs.fuga.inject.InjectorBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ApplicationContextBuilder {

    private Environment environment = Environment.DEFAULT;
    private List<Unit> units = new LinkedList<>();

    public ApplicationContextBuilder() {
    }

    public ApplicationContextBuilder withEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public ApplicationContextBuilder withUnits(Unit... units) {
        this.units.addAll(Arrays.asList(units));
        return this;
    }

    public ApplicationContext build() {
        var context = new StaticApplicationContext(environment);
        var eventDispatcher = new DefaultApplicationEventDispatcher();
        var eventPublisher = new DefaultApplicationEventPublisher(eventDispatcher);
        var eventManager = new DefaultApplicationEventManager(eventDispatcher);

        units.add((c) -> {
            c.bind(ApplicationContext.class).toInstance(context);
            c.bind(ConfigurableApplicationContext.class).toInstance(context);

            c.bind(ApplicationEventDispatcher.class).toInstance(eventDispatcher);
            c.bind(ApplicationEventPublisher.class).toInstance(eventPublisher);
            c.bind(ApplicationEventManager.class).toInstance(eventManager);
        });

        var injector = new InjectorBuilder().withUnits(units).build();

        context.setInjector(injector);
        context.init();

        return context;
    }
}
