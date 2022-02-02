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

import fuga.common.Key;
import fuga.inject.Unit;

import java.util.Arrays;

public final class FugaBoot {

    private FugaBoot() {
    }

    public static <T> T start(Class<T> applicationClass, Unit... units) {
        return start(applicationClass, Arrays.asList(units));
    }

    public static <T> T start(Key<T> applicationClass, Unit... units) {
        return start(applicationClass, Arrays.asList(units));
    }

    public static <T> T start(Class<T> applicationClass, Iterable<? extends Unit> units) {
        return start(Key.of(applicationClass), units);
    }

    public static <T> T start(Key<T> applicationClass, Iterable<? extends Unit> units) {
        var context = new ApplicationContextBuilder().withUnits(units).build();
        var injector = context.getInjector();

        var configurableContext = injector.getInstance(ConfigurableApplicationContext.class);

        var application = injector.getInstance(applicationClass);

        configurableContext.init();
        configurableContext.start();

        return application;
    }
}
