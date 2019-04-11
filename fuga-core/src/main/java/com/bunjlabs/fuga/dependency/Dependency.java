package com.bunjlabs.fuga.dependency;

public class Dependency {
    private Class targetClass;

    private Object targetInstance;

    public Dependency() {
    }

    public Dependency(Object targetInstance) {
        this.targetInstance = targetInstance;
    }

    public Dependency(Class targetClass, Object targetInstance) {
        this.targetClass = targetClass;
        this.targetInstance = targetInstance;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public Object getTargetInstance() {
        return targetInstance;
    }

    public void setTargetInstance(Object targetInstance) {
        this.targetInstance = targetInstance;
    }
}
