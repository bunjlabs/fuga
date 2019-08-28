/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.examples.minimal;

import com.bunjlabs.fuga.context.ApplicationContext;
import com.bunjlabs.fuga.examples.minimal.services.TestService;
import com.bunjlabs.fuga.examples.minimal.units.AppUnit;
import com.bunjlabs.fuga.inject.Injector;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TestApp {

    public static void main(String[] args) {
        ApplicationContext context = ApplicationContext.fromUnit(new AppUnit());

        Injector injector = context.getInjector();

        TestService testService = injector.getInstance(TestService.class);
    }

}
