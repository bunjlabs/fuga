/*
 * Copyright 2019-2021 Bunjlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fuga.inject.support;

import fuga.common.annotation.AnnotationUtils;
import fuga.common.errors.ErrorMessages;
import fuga.inject.ConfigurationException;
import fuga.inject.ScopeAnnotation;
import fuga.util.Assert;

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
