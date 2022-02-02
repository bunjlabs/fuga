package fuga.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = {PARAMETER})
@Retention(value = RUNTIME)
public @interface InjectHint {

    Target target() default Target.BINDING;

    boolean nullable() default false;

    boolean all() default false;

    enum Target {
        BINDING, ATTRIBUTE, SOURCE
    }
}
