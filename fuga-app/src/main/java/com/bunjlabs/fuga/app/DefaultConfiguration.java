package com.bunjlabs.fuga.app;

import java.util.LinkedList;
import java.util.List;

public class DefaultConfiguration implements Configuration {

    private List<Class> classRegisterList = new LinkedList<>();
    private List<ObjectDefinition> objectRegisterList = new LinkedList<>();

    public List<Class> getClassRegisterList() {
        return classRegisterList;
    }

    public List<ObjectDefinition> getObjectRegisterList() {
        return objectRegisterList;
    }

    @Override
    public void add(Class targetClass) {
        classRegisterList.add(targetClass);
    }

    @Override
    public <T> void add(Class<T> targetClass, T targetInstance) {
        objectRegisterList.add(new ObjectDefinition(targetClass, targetInstance));
    }

    public static class ObjectDefinition {
        private Class type;
        private Object instance;

        public ObjectDefinition(Class type, Object instance) {
            this.type = type;
            this.instance = instance;
        }

        public Class getType() {
            return type;
        }

        public Object getInstance() {
            return instance;
        }
    }
}
