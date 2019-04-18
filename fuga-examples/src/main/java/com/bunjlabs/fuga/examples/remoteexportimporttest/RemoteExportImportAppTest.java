package com.bunjlabs.fuga.examples.remoteexportimporttest;

import com.bunjlabs.fuga.app.StaticApplicationContext;
import com.bunjlabs.fuga.remoting.RemoteCall;
import com.bunjlabs.fuga.remoting.RemoteCallResult;
import com.bunjlabs.fuga.remoting.RemoteExporter;

public class RemoteExportImportAppTest {

    public static void main(String[] args) throws Exception {
        StaticApplicationContext applicationContext = new StaticApplicationContext();

        applicationContext.runConfigurator((c) -> {
            c.add(TestExportedServiceImpl.class);
        });

        applicationContext.runConfigurator(RemoteExportConfigurator.class);

        RemoteExporter remoteExporter = applicationContext.getIocContainer().getService(RemoteExporter.class);

        RemoteCallResult callResult = remoteExporter.handleCall(new RemoteCall("test", new Class[]{int.class, int.class}, new Object[]{5, 7}));

        System.out.println(callResult);
    }
}
