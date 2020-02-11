/*
 * Copyright 2019 Bunjlabs
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

package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.common.annotation.AnnotationUtils;
import com.bunjlabs.fuga.common.errors.ErrorMessages;
import com.bunjlabs.fuga.inject.*;
import com.bunjlabs.fuga.inject.bindings.*;

import java.lang.reflect.Constructor;

class DefaultBindingProcessor extends AbstractBindingProcessor {

    DefaultBindingProcessor(Container container, ErrorMessages errorMessages) {
        super(container, errorMessages);
    }

    private <T> InternalFactory<T> scope(Key<T> key, Scoping scoping, InternalFactory<T> internalFactory) {
        if (scoping == null || scoping == Scoping.UNSCOPED) {
            return internalFactory;
        }

        Scope scope;
        if (scoping.getScopeInstance() != null) {
            scope = scoping.getScopeInstance();
        } else {
            var scopeBinding = getScopeBinding(scoping.getScopeAnnotation());
            scope = scopeBinding.getScope();
        }

        if (scope == null) {
            Errors.noScopeBinding(getErrorMessages(), scoping);
            return internalFactory;
        }

        var providerAdapter = new ProviderToInternalFactoryAdapter<>(internalFactory);
        scheduleInitialization(providerAdapter);
        var scopedProvider = scope.scope(key, providerAdapter);
        return new ProviderInstanceFactory<>(scopedProvider);
    }

    @Override
    public <T> boolean process(Binding<T> binding) {
        return binding.acceptVisitor(new AbstractBindingVisitor<>((AbstractBinding<T>) binding) {

            @Override
            public Boolean visit(InstanceBinding<? extends T> binding) {
                var instance = binding.getInstance();
                var internalFactory = scope(key, scoping, new ProviderInstanceFactory<>(() -> instance));
                putBinding(new InstanceBindingImpl<>(key, instance, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ConstructorBinding<? extends T> binding) {
                var injectionPoint = binding.getInjectionPoint();
                @SuppressWarnings("unchecked")
                var constructor = (Constructor<T>) injectionPoint.getMember();
                var constructorInjector = new ConstructorInjector<>(injectionPoint, new ReflectConstructionProxy<>(constructor));
                var internalFactory = scope(key, scoping, new ConstructorFactory<>(constructorInjector));
                putBinding(new ConstructorBindingImpl<>(key, injectionPoint, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ProviderKeyBinding<? extends T> binding) {
                var providerKey = binding.getProviderKey();
                if (providerKey.equals(binding.getKey())) {
                    Errors.recursiveProviderType(getErrorMessages());
                    return false;
                }
                var internalFactory = scope(key, scoping, new DelegatedProviderFactory<>(providerKey));
                putBinding(new ProviderKeyBindingImpl<>(key, providerKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ProviderBinding<? extends T> binding) {
                var provider = binding.getProvider();
                var internalFactory = scope(key, scoping, new ProviderInstanceFactory<>(provider::get));
                putBinding(new ProviderBindingImpl<>(key, provider, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ComposerKeyBinding<? extends T> binding) {
                var composerKey = binding.getComposerKey();
                if (composerKey.equals(binding.getKey())) {
                    Errors.recursiveComposerType(getErrorMessages());
                    return false;
                }
                var internalFactory = scope(key, scoping, new DelegatedComposerFactory<>(composerKey));
                putBinding(new ComposerKeyBindingImpl<>(key, composerKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ComposerBinding<? extends T> binding) {
                var composer = binding.getComposer();
                var internalFactory = scope(key, scoping, new ComposerInstanceFactory<>(composer));
                putBinding(new ComposerBindingImpl<>(key, composer, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(LinkedKeyBinding<? extends T> binding) {
                if (binding.getKey().equals(binding.getLinkedKey())) {
                    Errors.recursiveBinding(getErrorMessages(), binding);
                }
                var linkKey = binding.getLinkedKey();
                var internalFactory = scope(key, scoping, new DelegatedKeyFactory<>(linkKey));
                putBinding(new LinkedKeyBindingImpl<>(key, linkKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(AutoBinding<? extends T> binding) {
                var providedBy = AnnotationUtils.findAnnotation(key.getRawType(), ProvidedBy.class);
                if (providedBy != null) {
                    @SuppressWarnings("unchecked")
                    var providerKey = (Key<? extends Provider<? extends T>>) Key.of(providedBy.value());
                    if (providerKey.equals(binding.getKey())) {
                        Errors.recursiveProviderType(getErrorMessages());
                        return false;
                    }
                    var internalFactory = scope(key, scoping, new DelegatedProviderFactory<>(providerKey));
                    putBinding(new ProviderKeyBindingImpl<>(key, providerKey, internalFactory));
                    return true;
                }

                var composedBy = AnnotationUtils.findAnnotation(key.getRawType(), ComposedBy.class);
                if (composedBy != null) {
                    var composerKey = Key.of(composedBy.value());
                    if (composerKey.equals(binding.getKey())) {
                        Errors.recursiveComposerType(getErrorMessages());
                        return false;
                    }
                    var internalFactory = scope(key, scoping, new DelegatedComposerFactory<>(composerKey));
                    putBinding(new ComposerKeyBindingImpl<>(key, composerKey, internalFactory));
                    return true;
                }

                var injectionPoint = InjectionPoint.forConstructorOf(key.getType());
                @SuppressWarnings("unchecked")
                var constructor = (Constructor<T>) injectionPoint.getMember();
                var constructorInjector = new ConstructorInjector<>(injectionPoint, new ReflectConstructionProxy<>(constructor));
                var internalFactory = scope(key, scoping, new ConstructorFactory<>(constructorInjector));
                putBinding(new ConstructorBindingImpl<>(key, injectionPoint, internalFactory));
                return true;
            }
        });
    }
}
