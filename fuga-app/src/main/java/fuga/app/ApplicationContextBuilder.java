/*
 * Copyright 2019-2021 Bunjlabs
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

package fuga.app;


import fuga.app.support.StaticApplicationContext;
import fuga.environment.Environment;
import fuga.event.EventUnit;
import fuga.inject.InjectorBuilder;
import fuga.inject.Singleton;
import fuga.inject.Unit;
import fuga.logging.LoggingUnit;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ApplicationContextBuilder {

    private final List<Unit> units = new LinkedList<>();
    private Environment environment = Environment.DEFAULT;

    public ApplicationContextBuilder() {
    }

    public ApplicationContextBuilder withUnits(Unit... units) {
        return withUnits(Arrays.asList(units));
    }

    public ApplicationContextBuilder withUnits(Iterable<? extends Unit> units) {
        units.forEach(this.units::add);
        return this;
    }

    public ApplicationContextBuilder withEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public ApplicationContext build() {
        var contextUnit = (Unit) (c) -> {
            c.depends(EventUnit.class);
            c.depends(LoggingUnit.class);

            c.bind(Environment.class).toInstance(environment);
            c.bind(StaticApplicationContext.class).in(Singleton.class);
            c.bind(ConfigurableApplicationContext.class).to(StaticApplicationContext.class);
            c.bind(ApplicationContext.class).to(StaticApplicationContext.class);
        };

        var injector = new InjectorBuilder()
                .withUnits(contextUnit)
                .withUnits(units)
                .build();

        var context = injector.getInstance(StaticApplicationContext.class);
        context.setInjector(injector);

        return context;
    }
}
