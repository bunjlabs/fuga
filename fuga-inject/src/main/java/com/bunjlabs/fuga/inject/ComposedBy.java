package com.bunjlabs.fuga.inject;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@Target(value = {TYPE, ANNOTATION_TYPE})
@Retention(value = RUNTIME)
public @interface ComposedBy {
    Class<? extends Composer> value();
}
