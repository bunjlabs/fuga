package com.bunjlabs.fuga.inject.support;

abstract class AbstractScopeBindingProcessor implements ScopeBindingProcessor {

    private final Container container;

    AbstractScopeBindingProcessor(Container container) {
        this.container = container;
    }

    public void putScopeBinding(ScopeBinding scopeBinding) {
        container.putScopeBinding(scopeBinding);
    }
}
