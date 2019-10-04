package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.common.errors.ErrorMessages;
import com.bunjlabs.fuga.inject.Binding;

import java.lang.annotation.Annotation;

abstract class Errors {

    static void missingScopeAnnotation(ErrorMessages errorMessages, Class<? extends Annotation> annotation) {
        errorMessages.addMessage("%s should be annotated with @ScopeAnnotation.", annotation);
    }

    static void missingRuntimeRetention(ErrorMessages errorMessages, Class<? extends Annotation> annotation) {
        errorMessages.addMessage("%s should be annotated with @Retention(RUNTIME).", annotation);
    }

    static void recursiveBinding(ErrorMessages errorMessages, Binding<?> binding) {
        errorMessages.addMessage("Binding %s points to itself.", binding);
    }

    static void noScopeBinding(ErrorMessages errorMessages, Scoping scoping) {
        errorMessages.addMessage("No %s scope binding found.", scoping);
    }

    static void recursiveProviderType(ErrorMessages errorMessages, Scoping scoping) {
        errorMessages.addMessage("@ProvidedBy points to the same class it annotates.", scoping);
    }

    static void recursiveProviderType(ErrorMessages errorMessages) {
        errorMessages.addMessage("@ProvidedBy points to the same class it annotates.");
    }

    static void recursiveComposerType(ErrorMessages errorMessages) {
        errorMessages.addMessage("@ComposedBy points to the same class it annotates.");
    }
}
