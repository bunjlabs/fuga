/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.dependency.support;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class DefaultMapBinder implements MapBinder {

    private final Map<Class, Class> bindingMap = new HashMap<>();

    @Override
    public void bind(Class<?> what, Class<?> to) {
        bindingMap.put(what, to);
    }

    @Override
    public Class getBindingFor(Class type) {
        return bindingMap.getOrDefault(type, type);
    }

}
