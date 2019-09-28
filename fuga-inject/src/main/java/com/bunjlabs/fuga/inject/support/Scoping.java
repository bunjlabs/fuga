package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Scope;
import com.bunjlabs.fuga.inject.Scopes;

import java.lang.annotation.Annotation;

abstract class Scoping {

    static final Scoping UNSCOPED =
            new Scoping() {
                @Override
                public Scope getScopeInstance() {
                    return Scopes.NO_SCOPE;
                }

                @Override
                public String toString() {
                    return Scopes.NO_SCOPE.toString();
                }
            };

    static Scoping forAnnotation(final Class<? extends Annotation> annotationType) {
        return new Scoping() {
            @Override
            public Class<? extends Annotation> getScopeAnnotation() {
                return annotationType;
            }

            @Override
            public String toString() {
                return annotationType.getName();
            }
        };
    }

    static Scoping forInstance(final Scope scope) {
        return new Scoping() {
            @Override
            public Scope getScopeInstance() {
                return scope;
            }

            @Override
            public String toString() {
                return scope.toString();
            }
        };
    }

    public Scope getScopeInstance() {
        return null;
    }

    public Class<? extends Annotation> getScopeAnnotation() {
        return null;
    }
}
