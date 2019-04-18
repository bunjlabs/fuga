package com.bunjlabs.fuga.examples.remoteexportimporttest;

import com.bunjlabs.fuga.app.Configuration;
import com.bunjlabs.fuga.app.Configurator;
import com.bunjlabs.fuga.dependency.annotations.Bind;
import com.bunjlabs.fuga.dependency.annotations.Inject;
import com.bunjlabs.fuga.remoting.RemoteExporter;

public class RemoteExportConfigurator implements Configurator {

    private TestExportedService service;

    @Inject(bindings = {
            @Bind(what = TestExportedService.class, to = TestExportedServiceImpl.class)
    })
    public RemoteExportConfigurator(TestExportedService service) {
        this.service = service;
    }

    @Override
    public void configure(Configuration configuration) {
        RemoteExporter exporter = new RemoteExporter();
        exporter.setServiceInterface(TestExportedService.class);
        exporter.setService(service);

        configuration.add(exporter);
    }
}
