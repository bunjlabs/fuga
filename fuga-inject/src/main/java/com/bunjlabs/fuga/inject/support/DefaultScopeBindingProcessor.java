package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.common.annotation.AnnotationUtils;
import com.bunjlabs.fuga.common.errors.ErrorMessages;
import com.bunjlabs.fuga.inject.ConfigurationException;
import com.bunjlabs.fuga.inject.ScopeAnnotation;
import com.bunjlabs.fuga.util.Assert;

class DefaultScopeBindingProcessor extends AbstractScopeBindingProcessor {

    DefaultScopeBindingProcessor(Container container, ErrorMessages errorMessages) {
        super(container, errorMessages);
    }

    @Override
    public boolean process(ScopeBinding scopeBinding) throws ConfigurationException {
        Assert.notNull(scopeBinding.getAnnotationType());
        Assert.notNull(scopeBinding.getScope());

        var annotationType = scopeBinding.getAnnotationType();
        if (!AnnotationUtils.hasAnnotation(annotationType, ScopeAnnotation.class)) {
            Errors.missingScopeAnnotation(getErrorMessages(), annotationType);
        }

        if (!AnnotationUtils.isRetainedAtRuntime(annotationType)) {
            Errors.missingRuntimeRetention(getErrorMessages(), annotationType);
        }

        putScopeBinding(scopeBinding);
        return true;
    }
}
