package com.bunjlabs.fuga.examples.remoteexportimporttest;

import com.bunjlabs.fuga.context.StaticApplicationContext;
import com.bunjlabs.fuga.examples.remoteexportimporttest.units.RemoteExportUnit;
import com.bunjlabs.fuga.examples.remoteexportimporttest.services.TestExportedService;
import com.bunjlabs.fuga.examples.remoteexportimporttest.services.TestExportedServiceImpl;
import com.bunjlabs.fuga.examples.remoteexportimporttest.settings.TextExportedServiceSettings;
import com.bunjlabs.fuga.remoting.RemoteCall;
import com.bunjlabs.fuga.remoting.RemoteCallResult;
import com.bunjlabs.fuga.remoting.RemoteExporter;
import com.bunjlabs.fuga.remoting.support.DefaultRemoteExporter;
import com.bunjlabs.fuga.settings.SettingsUnitBuilder;
import com.bunjlabs.fuga.settings.source.LocalFilesSettingsSource;

public class RemoteExportImportAppTest {

    public static void main(String[] args) throws Exception {
        StaticApplicationContext applicationContext = new StaticApplicationContext();

        // Settings
        applicationContext.insertModule(new SettingsUnitBuilder()
                .withSettingsSources(new LocalFilesSettingsSource(".", "input.yaml"))
                .withInterfaces(TextExportedServiceSettings.class)
                .build());

        // Exported service
        applicationContext.insertModule((c) ->
                c.add(TestExportedService.class, TestExportedServiceImpl.class));

        // Remote exporter
        applicationContext.insertModule(RemoteExportUnit.class);

        // Test
        RemoteExporter remoteExporter = applicationContext.getIocContainer().getService(DefaultRemoteExporter.class);
        RemoteCallResult callResult = remoteExporter.handleCall(new RemoteCall("test", new Class[]{int.class, int.class}, new Object[]{5, 7}));
        System.out.println(callResult);
    }
}
