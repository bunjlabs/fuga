package fuga.inject;

@FunctionalInterface
public interface Encounter<T> {

    void configure(Binding<T> binding) throws ConfigurationException;
}
