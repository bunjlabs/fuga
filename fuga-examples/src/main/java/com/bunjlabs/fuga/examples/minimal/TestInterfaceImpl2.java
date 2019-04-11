/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.examples.minimal;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TestInterfaceImpl2 implements TestInterface {

    @Override
    public int getCounter() {
        return 0;
    }

    @Override
    public void setCounter(int counter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
