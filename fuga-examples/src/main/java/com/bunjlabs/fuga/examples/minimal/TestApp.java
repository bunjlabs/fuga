/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.examples.minimal;

import com.bunjlabs.fuga.context.ApplicationContext;
import com.bunjlabs.fuga.context.FugaBoot;
import com.bunjlabs.fuga.examples.minimal.services.TestInterface;
import com.bunjlabs.fuga.examples.minimal.services.TestInterfaceImpl;
import com.bunjlabs.fuga.examples.minimal.services.TestService;
import com.bunjlabs.fuga.examples.minimal.services.TestServiceImpl;
import com.bunjlabs.fuga.examples.minimal.settings.FirstHttpSettings;
import com.bunjlabs.fuga.inject.Injector;
import com.bunjlabs.fuga.settings.SettingsUnitBuilder;
import com.bunjlabs.fuga.settings.source.LocalFilesSettingsSource;
import com.bunjlabs.fuga.util.FullType;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TestApp {

    public static void main(String[] args) {
        ApplicationContext context = FugaBoot.start(c -> {
            c.install(new SettingsUnitBuilder()
                    .withSettingsSources(new LocalFilesSettingsSource("."))
                    .build());

            c.bind(FirstHttpSettings.class).auto();

            c.bind(TestServiceImpl.class).auto();
            c.bind(TestInterfaceImpl.class).auto();

            c.bind(TestService.class).to(TestServiceImpl.class);
            c.bind(TestInterface.class).to(TestInterfaceImpl.class);
        });

        Injector injector = context.getInjector();

        TestService testService = injector.getInstance(TestService.class);

        FullType<? extends TestService> fullType = FullType.of(testService.getClass());
        FullType superType = fullType.getSuperType();
        FullType[] interfaces = fullType.getInterfaces();
        FullType[] generics = fullType.getGenerics();
        System.out.println(testService.test());
        System.out.println(testService.test());
        System.out.println(testService.test());
    }

}
