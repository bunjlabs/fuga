/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.examples.minimal;

import com.bunjlabs.fuga.dependency.annotations.Bind;
import com.bunjlabs.fuga.dependency.annotations.Inject;
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
    private final TestRemoteService remoteService;

    private int counter = 0;

    @Inject(bindings = {
            @Bind(what = TestInterface.class, to = TestInterfaceImpl1.class)
    })
    public TestServiceImpl(TestInterface testInterface, TestRemoteService remoteService) {
        this.testInterface = testInterface;
        this.remoteService = remoteService;
    }

    @RpcMethod
    public String test() {
        testInterface.setCounter(counter++);

        return testInterface.getCounter() + " - " + remoteService.execute();
    }

}
