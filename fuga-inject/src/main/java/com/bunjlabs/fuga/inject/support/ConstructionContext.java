package com.bunjlabs.fuga.inject.support;

public class ConstructionContext<T> {

    private boolean constructing = false;
    private T reference;

    boolean isConstructing() {
        return constructing;
    }

    void startConstruction() {
        this.constructing = true;
    }

    void endConstruction() {
        this.constructing = false;
    }

    T getReference() {
        return reference;
    }

    void setReference(T reference) {
        this.reference = reference;
    }
}
