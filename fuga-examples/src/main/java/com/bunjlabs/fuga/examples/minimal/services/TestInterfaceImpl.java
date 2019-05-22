/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.examples.minimal.services;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class TestInterfaceImpl implements TestInterface {

    private String message = "Hello world!";
    private int counter;

    public TestInterfaceImpl() {
    }

    @Override
    public int getCounter() {
        return counter;
    }

    @Override
    public void setCounter(int counter) {
        this.counter = counter;
    }
}
