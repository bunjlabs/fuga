package fuga.util;

import fuga.lang.FullType;

public abstract class Matchers {

    private static final Matcher<Object> ANY = t -> true;

    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> any() {
        return (Matcher<T>) ANY;
    }

    public static <T> Matcher<T> not(Matcher<T> matcher) {
        return t -> !matcher.match(t);
    }

    public static Matcher<FullType<?>> exact(Class<?> type) {
        return exact(FullType.of(type));
    }

    public static Matcher<FullType<?>> exact(FullType<?> type) {
        return type::equals;
    }
}
