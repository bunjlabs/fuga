package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.Binding;
import fuga.inject.Scope;

class ScopeKeyFactory<T> implements InternalFactory<T>, Initializable {
    private final Binding<T> binding;
    private final Key<? extends Scope> scopeKey;

    private InternalFactory<T> internalFactory;

    ScopeKeyFactory(Binding<T> binding, Key<? extends Scope> scopeKey, InternalFactory<T> internalFactory) {
        this.binding = binding;
        this.scopeKey = scopeKey;
        this.internalFactory = internalFactory;
    }

    @Override
    public void initialize(InjectorImpl injector) {
        var scope = injector.getInstance(scopeKey);
        var providerAdapter = new ProviderToInternalFactoryAdapter<>(internalFactory);
        providerAdapter.initialize(injector);

        var scopedProvider = scope.scope(binding.getKey(), providerAdapter);
        if (scopedProvider != null && scopedProvider != providerAdapter) {
            this.internalFactory = new ProviderInstanceFactory<>(scopedProvider);
        }
    }

    @Override
    public T get(InjectorContext context) throws InternalProvisionException {
        return internalFactory.get(context);
    }
}
