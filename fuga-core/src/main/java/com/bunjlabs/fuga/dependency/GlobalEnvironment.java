/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.dependency;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class GlobalEnvironment implements Environment {

    public GlobalEnvironment() {
    }

    @Override
    public void configure(Binder binder) {
        // Do nothing. Bind all dependencies as is.
    }

}
