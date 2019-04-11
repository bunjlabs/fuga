/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.examples.minimal;

import com.bunjlabs.fuga.app.FugaApp;
import com.bunjlabs.fuga.dependency.Binder;
import com.bunjlabs.fuga.dependency.Environment;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TestServiceEnvironment implements Environment {

    @Override
    public void configure(Binder binder) {
        binder.bind(FugaApp.class, TestApp.class);
        //binder.bind(TestInterface.class, TestInterfaceImpl2.class);
    }

}
