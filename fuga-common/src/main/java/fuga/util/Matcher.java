package fuga.util;

@FunctionalInterface
public interface Matcher<T> {

    boolean match(T t);

    default Matcher<T> and(Matcher<T> other) {
        return t -> match(t) && other.match(t);
    }

    default Matcher<T> or(Matcher<T> other) {
        return t -> match(t) || other.match(t);
    }
}
