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

package com.bunjlabs.fuga.context.support;

import com.bunjlabs.fuga.environment.Environment;
import com.bunjlabs.fuga.inject.Injector;

public class StaticApplicationContext extends AbstractApplicationContext {
    private final Environment environment;
    private Injector injector;

    public StaticApplicationContext(Environment environment) {
        super();
        this.environment = environment;
    }

    @Override
    public Injector getInjector() {
        return injector;
    }

    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

}
