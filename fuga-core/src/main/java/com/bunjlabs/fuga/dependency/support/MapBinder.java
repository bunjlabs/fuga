/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.dependency.support;

import com.bunjlabs.fuga.dependency.Binder;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public interface MapBinder extends Binder {

    Class getBindingFor(Class type);
}
