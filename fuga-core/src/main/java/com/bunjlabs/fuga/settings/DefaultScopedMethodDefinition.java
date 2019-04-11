package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.util.Assert;

import java.lang.reflect.Method;
import java.util.Objects;

public class DefaultScopedMethodDefinition implements ScopedMethodDefinition {

    private final Class type;
    private final Method method;

    public DefaultScopedMethodDefinition(Class type, Method method) {
        Assert.notNull(type);
        Assert.notNull(method);

        this.type = type;
        this.method = method;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public boolean isMatch(Method method) {
        return method.getName().equals(this.method.getName())
                && method.getDeclaringClass() == this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultScopedMethodDefinition that = (DefaultScopedMethodDefinition) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, method);
    }

    @Override
    public String toString() {
        return "ScopedMethodDefinition{" +
                "type=" + type +
                ", method='" + method + '\'' +
                '}';
    }
}
