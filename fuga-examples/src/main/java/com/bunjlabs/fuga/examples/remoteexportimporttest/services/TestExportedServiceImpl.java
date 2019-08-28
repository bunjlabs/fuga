package com.bunjlabs.fuga.examples.remoteexportimporttest.services;

import com.bunjlabs.fuga.examples.remoteexportimporttest.settings.TextExportedServiceSettings;
import com.bunjlabs.fuga.inject.Inject;

public class TestExportedServiceImpl implements TestExportedService {

    private final TextExportedServiceSettings serviceSettings;

    @Inject
    public TestExportedServiceImpl(TextExportedServiceSettings serviceSettings) {
        this.serviceSettings = serviceSettings;
    }

    @Override
    public String test(int a, int b) {
        if (a == b) throw new RuntimeException("Oops");

        return "Result is:" + (a * b + serviceSettings.intValue()) + " - " + serviceSettings.stringValue();
    }
}
