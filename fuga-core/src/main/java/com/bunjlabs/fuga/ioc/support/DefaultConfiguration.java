package com.bunjlabs.fuga.ioc.support;

import com.bunjlabs.fuga.ioc.Binding;
import com.bunjlabs.fuga.ioc.Configuration;
import com.bunjlabs.fuga.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class DefaultConfiguration implements Configuration {

    private List<Binding> bindings = new ArrayList<>();

    public List<Binding> getBindings() {
        return bindings;
    }

    @Override
    public void add(Class targetClass) {
        Assert.notNull(targetClass);

        bindings.add(new DefaultBinding(targetClass, targetClass));
    }

    @Override
    public void add(Object targetInstance) {
        Assert.notNull(targetInstance);

        bindings.add(new DefaultBinding(targetInstance.getClass(), targetInstance));
    }

    @Override
    public void add(Class<?> targetInterface, Class<?> prototype) {
        Assert.notNull(targetInterface);
        Assert.notNull(prototype);

        bindings.add(new DefaultBinding(targetInterface, prototype));
    }

    @Override
    public void add(Class<?> targetClass, Object targetInstance) {
        Assert.notNull(targetClass);
        Assert.notNull(targetInstance);

        bindings.add(new DefaultBinding(targetClass, targetInstance));
    }
}
