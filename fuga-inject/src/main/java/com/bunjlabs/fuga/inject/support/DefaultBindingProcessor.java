package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.common.annotation.AnnotationUtils;
import com.bunjlabs.fuga.inject.*;
import com.bunjlabs.fuga.inject.bindings.*;

import java.lang.reflect.Constructor;

class DefaultBindingProcessor extends AbstractBindingProcessor {

    DefaultBindingProcessor(Container container) {
        super(container);
    }

    private static <T> InternalFactory<T> scope(Scoping scoping, InternalFactory<T> internalFactory) {
        if (scoping == null) {
            return internalFactory;
        } else {
            return new ScopedFactory<>(internalFactory, scoping.getScopeInstance());
        }
    }

    @Override
    public <T> boolean process(Binding<T> binding) {
        return binding.acceptVisitor(new AbstractBindingVisitor<>((AbstractBinding<T>) binding) {

            @Override
            public Boolean visit(InstanceBinding<? extends T> binding) {
                var instance = binding.getInstance();
                var internalFactory = scope(scoping,
                        new ProviderInstanceFactory<>(() -> instance));
                putBinding(new InstanceBindingImpl<>(key, instance, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ConstructorBinding<? extends T> binding) {
                var injectionPoint = binding.getInjectionPoint();
                @SuppressWarnings("unchecked")
                var constructor = (Constructor<T>) injectionPoint.getMember();
                var internalFactory = scope(scoping,
                        new ConstructorFactory<>(new ReflectConstructionProxy<>(constructor)));
                putBinding(new ConstructorBindingImpl<>(key, injectionPoint, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ProviderKeyBinding<? extends T> binding) {
                var providerKey = binding.getProviderKey();
                var internalFactory = scope(scoping,
                        new DelegatedProviderFactory<>(providerKey));
                putBinding(new ProviderKeyBindingImpl<>(key, providerKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ProviderBinding<? extends T> binding) {
                var provider = binding.getProvider();
                var internalFactory = scope(scoping,
                        new ProviderInstanceFactory<T>(provider::get));
                putBinding(new ProviderBindingImpl<>(key, provider, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ComposerKeyBinding<? extends T> binding) {
                var composerKey = binding.getComposerKey();
                var internalFactory = scope(scoping,
                        new DelegatedComposerFactory<T>(composerKey));
                putBinding(new ComposerKeyBindingImpl<>(key, composerKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ComposerBinding<? extends T> binding) {
                var composer = binding.getComposer();
                var internalFactory = scope(scoping,
                        new ComposerInstanceFactory<T>(composer));
                putBinding(new ComposerBindingImpl<>(key, composer, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(LinkedKeyBinding<? extends T> binding) {
                var linkKey = binding.getLinkedKey();
                var internalFactory = scope(scoping,
                        new DelegatedKeyFactory<T>(linkKey));
                putBinding(new LinkedKeyBindingImpl<>(key, linkKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(AutoBinding<? extends T> binding) {
                var providedBy = AnnotationUtils.findAnnotation(key.getType(), ProvidedBy.class);
                if (providedBy != null) {
                    @SuppressWarnings("unchecked")
                    var providerKey = (Key<? extends Provider<? extends T>>) Key.of(providedBy.value());
                    var internalFactory = scope(scoping,
                            new DelegatedProviderFactory<>(providerKey));
                    putBinding(new ProviderKeyBindingImpl<>(key, providerKey, internalFactory));
                    return true;
                }

                var composedBy = AnnotationUtils.findAnnotation(key.getType(), ComposedBy.class);
                if (composedBy != null) {
                    var composerKey = Key.of(composedBy.value());
                    var internalFactory = scope(scoping,
                            new DelegatedComposerFactory<T>(composerKey));
                    putBinding(new ComposerKeyBindingImpl<>(key, composerKey, internalFactory));
                    return true;
                }

                var injectionPoint = InjectionPoint.forConstructorOf(key);
                @SuppressWarnings("unchecked")
                var constructor = (Constructor<T>) injectionPoint.getMember();
                var internalFactory = scope(scoping,
                        new ConstructorFactory<>(new ReflectConstructionProxy<>(constructor)));
                putBinding(new ConstructorBindingImpl<>(key, injectionPoint, internalFactory));
                return true;
            }
        });
    }
}
