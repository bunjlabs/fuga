/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.rpc.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
@Target(value = {TYPE})
@Retention(value = RUNTIME)
public @interface Rpc {

    MethodFamily methodFamily() default @MethodFamily;

    ParameterMapping parameterMapping() default @ParameterMapping;
}
