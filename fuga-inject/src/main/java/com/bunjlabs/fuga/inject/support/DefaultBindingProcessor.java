package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.annotation.AnnotationUtils;
import com.bunjlabs.fuga.inject.*;

import java.lang.reflect.Constructor;

public class DefaultBindingProcessor extends AbstractBindingProcessor {
    DefaultBindingProcessor(Container container) {
        super(container);
    }

    @Override
    public <T> boolean process(Binding<T> binding) {
        return binding.acceptVisitor(new AbstractBindingVisitor<>(binding) {

            @Override
            public Boolean visit(InstanceBinding<? extends T> binding) {
                var instance = binding.getInstance();
                var internalFactory = new InitializableFactory<>(() -> instance);
                putBinding(new InstanceBinding<>(key, instance, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ConstructorBinding<? extends T> binding) {
                var injectionPoint = binding.getInjectionPoint();
                @SuppressWarnings("unchecked")
                var constructor = (Constructor<T>) injectionPoint.getMember();
                var internalFactory = new ConstructorFactory<>(new ReflectConstructionProxy<>(constructor));
                putBinding(new ConstructorBinding<>(key, injectionPoint, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ProviderBinding<? extends T> binding) {
                var providerKey = binding.getProviderKey();
                var internalFactory = new DelegatedProviderFactory<>(providerKey);
                putBinding(new ProviderBinding<>(key, providerKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ProviderInstanceBinding<? extends T> binding) {
                var provider = binding.getProvider();
                var internalFactory = new InitializableFactory<T>(provider::get);
                putBinding(new ProviderInstanceBinding<>(key, provider, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ComposerBinding<? extends T> binding) {
                var composerKey = binding.getComposerKey();
                var internalFactory = new DelegatedComposerFactory<T>(composerKey);
                putBinding(new ComposerBinding<>(key, composerKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(ComposerInstanceBinding<? extends T> binding) {
                var composer = binding.getComposer();
                var internalFactory = new ProxyFactory<T>(composer);
                putBinding(new ComposerInstanceBinding<>(key, composer, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(LinkedKeyBinding<? extends T> binding) {
                var linkKey = binding.getLinkedKey();
                var internalFactory = new DelegatedKeyFactory<T>(linkKey);
                putBinding(new LinkedKeyBinding<>(key, linkKey, internalFactory));
                return true;
            }

            @Override
            public Boolean visit(AutoBinding<? extends T> binding) {
                var providedBy = AnnotationUtils.findAnnotation(key.getType(), ProvidedBy.class);
                if (providedBy != null) {
                    @SuppressWarnings("unchecked")
                    var providerKey = (Key<? extends Provider<? extends T>>) Key.of(providedBy.value());
                    var internalFactory = new DelegatedProviderFactory<>(providerKey);
                    putBinding(new ProviderBinding<>(key, providerKey, internalFactory));
                    return true;
                }

                var composedBy = AnnotationUtils.findAnnotation(key.getType(), ComposedBy.class);
                if (composedBy != null) {
                    var composerKey = Key.of(composedBy.value());
                    var internalFactory = new DelegatedComposerFactory<T>(composerKey);
                    putBinding(new ComposerBinding<>(key, composerKey, internalFactory));
                    return true;
                }

                var injectionPoint = InjectionPoint.forConstructorOf(key);
                @SuppressWarnings("unchecked")
                var constructor = (Constructor<T>) injectionPoint.getMember();
                var constructorFactory = new ConstructorFactory<>(new ReflectConstructionProxy<>(constructor));

                InternalFactory<T> internalFactory;
                if (injectionPoint.containsAnnotation(Singleton.class)) {
                    internalFactory = new SingletonCachedFactory<>(constructorFactory);
                } else {
                    internalFactory = constructorFactory;
                }
                putBinding(new ConstructorBinding<>(key, injectionPoint, internalFactory));
                return true;
            }
        });
    }
}
