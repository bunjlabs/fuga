/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.examples.minimal;

import com.bunjlabs.fuga.context.StaticApplicationContext;
import com.bunjlabs.fuga.examples.minimal.units.AppUnit;
import com.bunjlabs.fuga.examples.minimal.services.TestRemoteService;
import com.bunjlabs.fuga.examples.minimal.services.TestService;
import com.bunjlabs.fuga.examples.minimal.settings.FirstHttpSettings;
import com.bunjlabs.fuga.remoting.RemoteAccessorUnitBuilder;
import com.bunjlabs.fuga.remoting.support.DefaultRemoteCallResult;
import com.bunjlabs.fuga.settings.SettingsUnitBuilder;
import com.bunjlabs.fuga.settings.source.LocalFilesSettingsSource;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TestApp {

    public static void main(String[] args) {
        StaticApplicationContext applicationContext = new StaticApplicationContext();

        // SettingsNode
        applicationContext.insertModule(new SettingsUnitBuilder()
                .withSettingsSources(new LocalFilesSettingsSource(".", "input.yaml"))
                .withInterfaces(FirstHttpSettings.class)
                .build());

        // Remote service
        applicationContext.insertModule(new RemoteAccessorUnitBuilder()
                .withRemoteExecutor(call -> new DefaultRemoteCallResult("test call result from dummy remote call"))
                .withInterfaces(TestRemoteService.class)
                .build());

        // Local services
        applicationContext.insertModule(AppUnit.class);

        // Test
        TestService testService = applicationContext.getIocContainer().getService(TestService.class);
        FirstHttpSettings httpSettings = applicationContext.getIocContainer().getService(FirstHttpSettings.class);

        System.out.println(testService.test());
        System.out.println(testService.test());
        System.out.println(testService.test());
        System.out.format("%s - %s:%d\n", httpSettings.name(), httpSettings.host(), httpSettings.port());
    }

}
