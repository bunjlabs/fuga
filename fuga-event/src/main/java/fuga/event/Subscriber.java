package fuga.event;

@FunctionalInterface
public interface Subscriber<T> {

    void onEvent(T event);
}
