package fuga.inject;

@FunctionalInterface
public interface ProvisionListener<T> {

    void provision(T instance);
}
