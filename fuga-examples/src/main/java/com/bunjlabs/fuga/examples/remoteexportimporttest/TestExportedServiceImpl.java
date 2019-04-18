package com.bunjlabs.fuga.examples.remoteexportimporttest;

public class TestExportedServiceImpl implements TestExportedService {
    @Override
    public String test(int a, int b) {
        return "Result is:" + (a + b);
    }
}
