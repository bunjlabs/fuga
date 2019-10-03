package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Provider;

class ProviderToInternalFactoryAdapter<T> implements Provider<T>, Initializable {

    private final InternalFactory<T> internalFactory;
    private InjectorImpl injector;

    ProviderToInternalFactoryAdapter(InternalFactory<T> internalFactory) {
        this.internalFactory = internalFactory;
    }

    @Override
    public T get() {
        if (injector == null) {
            throw new IllegalStateException("Injector is not ready");
        }
        var ctx = injector.getContext();
        var dependency = ctx.getDependency();

        try {
            return internalFactory.get(ctx, dependency);
        } catch (InternalProvisionException e) {
            throw e.toProvisionException();
        }
    }

    @Override
    public void initialize(InjectorImpl injector) {
        this.injector = injector;
    }
}
