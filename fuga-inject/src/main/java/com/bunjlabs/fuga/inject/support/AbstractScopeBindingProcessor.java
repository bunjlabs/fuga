package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.common.errors.ErrorMessages;

abstract class AbstractScopeBindingProcessor implements ScopeBindingProcessor {

    private final Container container;
    private final ErrorMessages errorMessages;

    AbstractScopeBindingProcessor(Container container, ErrorMessages errorMessages) {
        this.container = container;
        this.errorMessages = errorMessages;
    }

    void putScopeBinding(ScopeBinding scopeBinding) {
        container.putScopeBinding(scopeBinding);
    }

    ErrorMessages getErrorMessages() {
        return errorMessages;
    }
}
