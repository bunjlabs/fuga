/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.dependency.annotations;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public @interface Bind {

    Class<?> what();

    Class<?> to();
}
