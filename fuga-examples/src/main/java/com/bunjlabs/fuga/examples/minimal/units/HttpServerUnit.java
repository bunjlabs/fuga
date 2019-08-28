package com.bunjlabs.fuga.examples.minimal.units;

import com.bunjlabs.fuga.examples.minimal.settings.HttpSettings;
import com.bunjlabs.fuga.inject.Configuration;
import com.bunjlabs.fuga.inject.Unit;

public class HttpServerUnit implements Unit {
    @Override
    public void configure(Configuration c) {
        c.bind(HttpSettings.class).to(HttpSettings.class);
    }
}
