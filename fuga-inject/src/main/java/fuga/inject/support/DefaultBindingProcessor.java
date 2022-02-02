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

import fuga.common.Key;
import fuga.common.annotation.AnnotationUtils;
import fuga.common.errors.ErrorMessages;
import fuga.inject.*;
import fuga.inject.bindings.*;

import java.lang.reflect.Constructor;
import java.util.Objects;
import java.util.stream.Collectors;

class DefaultBindingProcessor extends AbstractBindingProcessor {

    DefaultBindingProcessor(Container container, ErrorMessages errorMessages) {
        super(container, errorMessages);
    }

    private <T> void encounters(AbstractBinding<T> binding) {
        var bindingEncounters = getEncounters(binding.getKey());

        if (bindingEncounters.isEmpty()) {
            return;
        }

        bindingEncounters.stream()
                .map(BindingEncounter::getEncounter)
                .filter(Objects::nonNull)
                .forEach(e -> e.configure(binding));

        bindingEncounters.stream()
                .map(BindingEncounter::getEncounterKey)
                .filter(Objects::nonNull)
                .filter(k -> !k.equals(binding.getKey()))
                .forEach(e -> scheduleInitialization(new InitializableEncounter<>(binding, e)));
    }

    private <T> InternalFactory<T> attachments(AbstractBinding<T> binding, InternalFactory<T> factory) {
        var attachments = getAttachments(binding.getKey());

        var attachKeys = attachments.stream()
                .map(BindingAttachment::getAttachmentKey)
                .filter(Objects::nonNull)
                .filter(k -> !k.equals(binding.getKey()))
                .collect(Collectors.toUnmodifiableList());

        if (attachKeys.isEmpty()) {
            return factory;
        }

        var attachmentFactory = new AttachmentFactory<>(factory, attachKeys);
        scheduleInitialization(attachmentFactory);

        return attachmentFactory;
    }

    private <T> InternalFactory<T> scoping(AbstractBinding<T> binding, InternalFactory<T> factory) {
        var scoping = binding.getScoping();
        if (scoping == null || scoping == Scoping.UNSCOPED) {
            return factory;
        }

        if (scoping.getScopeInstance() != null) {
            var scope = scoping.getScopeInstance();
            var providerAdapter = new ProviderToInternalFactoryAdapter<>(factory);
            scheduleInitialization(providerAdapter);
            var scopedProvider = scope.scope(binding.getKey(), providerAdapter);
            if (scopedProvider == null || scopedProvider == providerAdapter) {
                return factory;
            }

            return new ProviderInstanceFactory<>(scopedProvider);
        }

        Key<? extends Scope> scopeKey;
        if (scoping.getScopeKey() != null) {
            scopeKey = scoping.getScopeKey();
        } else {
            var annotation = scoping.getScopeAnnotation();
            var scopeAnnotation = AnnotationUtils.findAnnotation(annotation, ScopeAnnotation.class);
            if (scopeAnnotation == null) {
                Errors.noScopedProvider(getErrorMessages(), scoping);
                return factory;
            }

            scopeKey = Key.of(scopeAnnotation.scope());
        }

        var scopeFactory = new ScopeKeyFactory<>(binding, scopeKey, factory);
        scheduleInitialization(scopeFactory);

        return scopeFactory;
    }

    private <T> InternalFactory<T> listeners(AbstractBinding<T> binding, InternalFactory<T> factory) {
        var watchings = getWatchings(binding.getKey());

        if (watchings.isEmpty()) {
            return factory;
        }

        var listeners = watchings.stream()
                .map(BindingWatching::getListener)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());


        if (!listeners.isEmpty()) {
            factory = new ListenersFactory<>(factory, listeners);
        }

        var listenerKeys = watchings.stream()
                .map(BindingWatching::getListenerKey)
                .filter(Objects::nonNull)
                .filter(k -> !k.equals(binding.getKey()))
                .collect(Collectors.toUnmodifiableList());

        if (!listenerKeys.isEmpty()) {
            var listenersFactory = new ListenerKeysFactory<>(factory, listenerKeys);
            scheduleInitialization(listenersFactory);

            factory = listenersFactory;
        }

        return factory;
    }

    private <T> InternalFactory<T> factory(AbstractBinding<T> binding, InternalFactory<T> factory) {
        encounters(binding);

        factory = attachments(binding, factory);
        factory = scoping(binding, factory);
        factory = listeners(binding, factory);

        return factory;
    }

    @Override
    public <T> boolean process(AbstractBinding<T> binding) {
        return binding.acceptVisitor(new AbstractBindingVisitor<>(binding) {

            @Override
            public Boolean visit(InstanceBinding<? extends T> instanceBinding) {
                var instance = instanceBinding.getInstance();
                var instanceFactory = new InstanceFactory<>(instance);

                binding.setAttribute(InternalFactory.class, factory(binding, instanceFactory));
                return true;
            }

            @Override
            public Boolean visit(ConstructorBinding<? extends T> constructorBinding) {
                var injectionPoint = constructorBinding.getInjectionPoint();
                @SuppressWarnings("unchecked")
                var constructor = (Constructor<T>) injectionPoint.getMember();
                var constructionProxy = new ReflectConstructionProxy<>(constructor);
                var constructorFactory = new ConstructorFactory<>(injectionPoint, constructionProxy);
                scheduleInitialization(constructorFactory);

                binding.setAttribute(InternalFactory.class, factory(binding, constructorFactory));
                return true;
            }

            @Override
            public Boolean visit(ProviderKeyBinding<? extends T> providerKeyBinding) {
                var providerKey = providerKeyBinding.getProviderKey();
                if (providerKey.equals(binding.getKey())) {
                    Errors.recursiveProviderType(getErrorMessages());
                    return false;
                }

                var delegatedProviderFactory = new DelegatedProviderFactory<>(providerKey);
                scheduleInitialization(delegatedProviderFactory);

                binding.setAttribute(InternalFactory.class, factory(binding, delegatedProviderFactory));
                return true;
            }

            @Override
            public Boolean visit(ProviderBinding<? extends T> providerBinding) {
                var provider = providerBinding.getProvider();
                var providerInstanceFactory = new ProviderInstanceFactory<T>(provider::get);

                binding.setAttribute(InternalFactory.class, factory(binding, providerInstanceFactory));
                return true;
            }

            @Override
            public Boolean visit(ComposerKeyBinding<? extends T> composerKeyBinding) {
                var composerKey = composerKeyBinding.getComposerKey();
                if (composerKey.equals(binding.getKey())) {
                    Errors.recursiveComposerType(getErrorMessages());
                    return false;
                }

                var delegatedComposerFactory = new DelegatedComposerFactory<>(key, composerKey);
                scheduleInitialization(delegatedComposerFactory);

                binding.setAttribute(InternalFactory.class, factory(binding, delegatedComposerFactory));
                return true;
            }

            @Override
            public Boolean visit(ComposerBinding<? extends T> composerBinding) {
                var composer = composerBinding.getComposer();
                var composerInstanceFactory = new ComposerInstanceFactory<>(key, composer);

                binding.setAttribute(InternalFactory.class, factory(binding, composerInstanceFactory));
                return true;
            }

            @Override
            public Boolean visit(LinkedKeyBinding<? extends T> linkedKeyBinding) {
                var linkKey = linkedKeyBinding.getLinkedKey();

                if (binding.getKey().equals(linkKey)) {
                    Errors.recursiveBinding(getErrorMessages(), binding);
                    return false;
                }

                var delegatedKeyFactory = new DelegatedKeyFactory<T>(linkKey);
                scheduleInitialization(delegatedKeyFactory);

                binding.setAttribute(InternalFactory.class, factory(binding, delegatedKeyFactory));
                return true;
            }

            @Override
            public Boolean visit(UntargetedBinding<? extends T> untargetedBinding) {
                var providedBy = AnnotationUtils.findAnnotation(key.getRawType(), ProvidedBy.class);
                if (providedBy != null) {
                    @SuppressWarnings("unchecked")
                    var providerKey = (Key<? extends Provider<? extends T>>) Key.of(providedBy.value());
                    if (providerKey.equals(binding.getKey())) {
                        Errors.recursiveProviderType(getErrorMessages());
                        return false;
                    }

                    var delegatedProviderFactory = new DelegatedProviderFactory<>(providerKey);
                    scheduleInitialization(delegatedProviderFactory);

                    binding.setAttribute(InternalFactory.class, factory(binding, delegatedProviderFactory));
                    return true;
                }

                var composedBy = AnnotationUtils.findAnnotation(key.getRawType(), ComposedBy.class);
                if (composedBy != null) {
                    var composerKey = Key.of(composedBy.value());
                    if (composerKey.equals(binding.getKey())) {
                        Errors.recursiveComposerType(getErrorMessages());
                        return false;
                    }

                    var delegatedComposerFactory = new DelegatedComposerFactory<>(key, composerKey);
                    scheduleInitialization(delegatedComposerFactory);

                    binding.setAttribute(InternalFactory.class, factory(binding, delegatedComposerFactory));
                    return true;
                }

                var injectionPoint = InjectionPoint.forConstructorOf(key.getType());
                @SuppressWarnings("unchecked")
                var constructor = (Constructor<T>) injectionPoint.getMember();
                var constructionProxy = new ReflectConstructionProxy<>(constructor);
                var constructorFactory = new ConstructorFactory<>(injectionPoint, constructionProxy);
                scheduleInitialization(constructorFactory);

                binding.setAttribute(InternalFactory.class, factory(binding, constructorFactory));
                return true;
            }
        });
    }
}
