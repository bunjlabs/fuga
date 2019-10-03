package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.common.annotation.AnnotationUtils;
import com.bunjlabs.fuga.inject.*;
import com.bunjlabs.fuga.inject.bindings.*;

import java.lang.reflect.Constructor;

class DefaultBindingProcessor extends AbstractBindingProcessor {

    DefaultBindingProcessor(Container container) {
        super(container);
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

        var providerAdapter = new ProviderToInternalFactoryAdapter<>(internalFactory);
        scheduleInitilization(providerAdapter);
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
                var linkKey = binding.getLinkedKey();
                var internalFactory = scope(key, scoping, new DelegatedKeyFactory<>(linkKey));
                putBinding(new LinkedKeyBindingImpl<>(key, linkKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(AutoBinding<? extends T> binding) {
                var providedBy = AnnotationUtils.findAnnotation(key.getType(), ProvidedBy.class);
                if (providedBy != null) {
                    @SuppressWarnings("unchecked")
                    var providerKey = (Key<? extends Provider<? extends T>>) Key.of(providedBy.value());
                    var internalFactory = scope(key, scoping, new DelegatedProviderFactory<>(providerKey));
                    putBinding(new ProviderKeyBindingImpl<>(key, providerKey, internalFactory));
                    return true;
                }

                var composedBy = AnnotationUtils.findAnnotation(key.getType(), ComposedBy.class);
                if (composedBy != null) {
                    var composerKey = Key.of(composedBy.value());
                    var internalFactory = scope(key, scoping, new DelegatedComposerFactory<>(composerKey));
                    putBinding(new ComposerKeyBindingImpl<>(key, composerKey, internalFactory));
                    return true;
                }

                var injectionPoint = InjectionPoint.forConstructorOf(key);
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
