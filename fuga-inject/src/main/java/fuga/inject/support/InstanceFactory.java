package fuga.inject.support;

public class InstanceFactory<T> implements InternalFactory<T> {
    private final T instance;

    public InstanceFactory(T instance) {
        this.instance = instance;
    }

    @Override
    public T get(InjectorContext context) throws InternalProvisionException {
        return instance;
    }
}
