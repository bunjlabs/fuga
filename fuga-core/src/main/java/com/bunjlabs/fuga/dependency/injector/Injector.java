/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.dependency.injector;

/**
 * @param <T>
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public interface Injector<T> {

    T inject(Object[] arguments) throws InjectException;

    Class[] getRequiredTypes();
}
