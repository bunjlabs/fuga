package com.bunjlabs.fuga.ioc.support;

import com.bunjlabs.fuga.ioc.BindingType;

class DefaultBinding implements com.bunjlabs.fuga.ioc.Binding {

    private final Class<?> target;

    private final Object instance;
    private final Class<?> prototype;

    DefaultBinding(Class<?> target, Object instance) {
        this.target = target;
        this.instance = instance;
        this.prototype = null;
    }

    DefaultBinding(Class<?> target, Class<?> prototype) {
        this.target = target;
        this.instance = null;
        this.prototype = prototype;
    }

    @Override
    public BindingType getBindingType() {
        if (instance != null) {
            return BindingType.INSTANCE;
        } else {
            return BindingType.PROTOTYPE;
        }
    }

    @Override
    public Class<?> getTarget() {
        return target;
    }

    @Override
    public Object getInstance() {
        return instance;
    }

    @Override
    public Class<?> getPrototype() {
        return prototype;
    }
}
