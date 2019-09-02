package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.inject.ComposedBy;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = {TYPE})
@Retention(value = RUNTIME)
@ComposedBy(SettingsComposer.class)
public @interface Settings {
    String value();
}
