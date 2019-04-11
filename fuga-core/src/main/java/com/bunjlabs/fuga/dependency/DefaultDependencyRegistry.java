/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.dependency;

import com.bunjlabs.fuga.dependency.injector.InjectException;
import com.bunjlabs.fuga.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class DefaultDependencyRegistry implements DependencyRegistry {

    private final Map<Class, Object> dependencies = new HashMap<>();

    public DefaultDependencyRegistry() {
    }

    @Override
    public void register(Class type, Object obj) {
        Assert.notNull(type);
        Assert.notNull(obj);

        dependencies.put(type, obj);
    }

    @Override
    public Object getDependency(Class type) throws InjectException {
        if (dependencies.containsKey(type)) {
            return dependencies.get(type);
        }

        throw new InjectException("No suitable dependency for " + type.getName());
    }
}
