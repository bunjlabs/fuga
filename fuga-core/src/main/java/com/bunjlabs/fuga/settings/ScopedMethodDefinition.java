package com.bunjlabs.fuga.settings;

import java.lang.reflect.Method;

public interface ScopedMethodDefinition {

    Method getMethod();

    boolean isMatch(Method method);

}
