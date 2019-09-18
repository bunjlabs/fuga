package com.bunjlabs.fuga.examples.minimal.units;

import com.bunjlabs.fuga.context.ApplicationListener;
import com.bunjlabs.fuga.context.events.ContextStartedEvent;
import com.bunjlabs.fuga.context.events.ContextStoppedEvent;
import com.bunjlabs.fuga.examples.minimal.settings.HttpSettings;
import com.bunjlabs.fuga.inject.Configuration;
import com.bunjlabs.fuga.inject.Unit;

public class HttpServerUnit implements Unit {
    @Override
    public void setup(Configuration c) {
        c.bind(HttpSettings.class).to(HttpSettings.class);
    }

}
