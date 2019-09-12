/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.examples.minimal.services;

import com.bunjlabs.fuga.examples.minimal.settings.FirstHttpSettings;
import com.bunjlabs.fuga.inject.Inject;
import com.bunjlabs.fuga.rpc.annotations.MethodFamily;
import com.bunjlabs.fuga.rpc.annotations.MethodFamilyPolicy;
import com.bunjlabs.fuga.rpc.annotations.Rpc;
import com.bunjlabs.fuga.rpc.annotations.RpcMethod;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
@Rpc(methodFamily = @MethodFamily(policy = MethodFamilyPolicy.CUSTOM, name = "test"))
public class TestServiceImpl implements TestService {

    private final TestInterface testInterface;
    private final FirstHttpSettings firstHttpSettings;

    private int counter = 0;

    @Inject
    public TestServiceImpl(TestInterface testInterface, FirstHttpSettings firstHttpSettings) {
        this.testInterface = testInterface;
        this.firstHttpSettings = firstHttpSettings;
    }

    @RpcMethod
    public String test() {
        testInterface.setCounter(counter++);

        return testInterface.getCounter() + " " + firstHttpSettings.name() + " " + firstHttpSettings.first().host() + ":" + firstHttpSettings.first().port();
    }

}
