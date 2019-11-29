/*
 * Copyright 2019 Bunjlabs
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.examples.minimal.services;

import com.bunjlabs.fuga.context.ApplicationEventManager;
import com.bunjlabs.fuga.context.ApplicationListener;
import com.bunjlabs.fuga.context.events.ContextStartedEvent;
import com.bunjlabs.fuga.examples.minimal.settings.FirstHttpSettings;
import com.bunjlabs.fuga.inject.Inject;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TestServiceImpl implements TestService {

    private final TestInterface testInterface;
    private final FirstHttpSettings firstHttpSettings;
    private final ApplicationEventManager eventManager;

    private int counter = 0;

    @Inject
    public TestServiceImpl(TestInterface testInterface, FirstHttpSettings firstHttpSettings, ApplicationEventManager eventManager) {
        this.testInterface = testInterface;
        this.firstHttpSettings = firstHttpSettings;
        this.eventManager = eventManager;

        ApplicationListener<ContextStartedEvent> listener = this::onStart;

        eventManager.addEventListener(listener);
    }

    private void onStart(ContextStartedEvent event) {
        System.out.println(event.getApplicationContext().getApplicationName());
    }

    public String test() {
        testInterface.setCounter(counter++);

        return testInterface.getCounter() + " " + firstHttpSettings.name() + " " + firstHttpSettings.first().host() + ":" + firstHttpSettings.first().port();
    }

}
