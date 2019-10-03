package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.common.annotation.AnnotationUtils;
import com.bunjlabs.fuga.inject.ScopeAnnotation;
import com.bunjlabs.fuga.util.Assert;

class DefaultScopeBindingProcessor extends AbstractScopeBindingProcessor {

    DefaultScopeBindingProcessor(Container container) {
        super(container);
    }

    @Override
    public boolean process(ScopeBinding scopeBinding) {
        Assert.notNull(scopeBinding.getAnnotationType());
        Assert.notNull(scopeBinding.getScope());

        var annotationType = scopeBinding.getAnnotationType();
        if (!AnnotationUtils.hasAnnotation(annotationType, ScopeAnnotation.class)) {
            // oops, there is no way to say about this mistake
            // TODO
        }

        if (AnnotationUtils.isRetainedAtRuntime(annotationType)) {
            // TODO the same
        }

        putScopeBinding(scopeBinding);
        return true;
    }
}
