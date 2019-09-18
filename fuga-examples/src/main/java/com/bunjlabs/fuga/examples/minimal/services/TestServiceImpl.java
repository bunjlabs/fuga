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
