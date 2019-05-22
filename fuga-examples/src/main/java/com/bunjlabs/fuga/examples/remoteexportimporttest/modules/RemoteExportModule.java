package com.bunjlabs.fuga.examples.remoteexportimporttest.modules;

import com.bunjlabs.fuga.examples.remoteexportimporttest.services.TestExportedService;
import com.bunjlabs.fuga.ioc.Configuration;
import com.bunjlabs.fuga.ioc.Inject;
import com.bunjlabs.fuga.ioc.Module;
import com.bunjlabs.fuga.remoting.RemoteExporter;
import com.bunjlabs.fuga.remoting.support.DefaultRemoteExporter;

public class RemoteExportModule implements Module {

    private TestExportedService service;

    @Inject
    public RemoteExportModule(TestExportedService service) {
        this.service = service;
    }

    @Override
    public void configure(Configuration configuration) {
        RemoteExporter exporter = new DefaultRemoteExporter(service, TestExportedService.class);

        configuration.add(exporter);
    }
}
