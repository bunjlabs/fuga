package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.inject.FabricatedBy;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = {TYPE})
@Retention(value = RUNTIME)
@FabricatedBy(SettingsFactory.class)
public @interface Settings {
    String value();
}
