/*
 * Copyright 2019-2020 Bunjlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
