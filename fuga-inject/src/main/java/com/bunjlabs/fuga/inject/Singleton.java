package com.bunjlabs.fuga.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = {CONSTRUCTOR})
@Retention(value = RUNTIME)
@ScopeAnnotation
public @interface Singleton {

}
