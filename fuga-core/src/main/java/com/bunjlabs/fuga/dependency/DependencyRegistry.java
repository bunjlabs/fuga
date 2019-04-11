/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.dependency;

import com.bunjlabs.fuga.dependency.injector.InjectException;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public interface DependencyRegistry {

    void register(Class type, Object obj);

    Object getDependency(Class type) throws InjectException;
}
