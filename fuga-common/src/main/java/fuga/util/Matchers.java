package fuga.util;

import fuga.lang.TypeLiteral;

import java.util.function.Predicate;

public abstract class Matchers {

    private static final Matcher<Object> ANY = t -> true;

    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> any() {
        return (Matcher<T>) ANY;
    }

    public static <T> Matcher<T> not(Matcher<T> matcher) {
        return t -> !matcher.match(t);
    }

    public static <T> Matcher<TypeLiteral<T>> only(Class<T> type) {
        return only(TypeLiteral.of(type));
    }

    public static <T> Matcher<TypeLiteral<T>> only(TypeLiteral<T> type) {
        return type::equals;
    }

    public static <T> Matcher<TypeLiteral<?>> like(Class<T> type) {
        return like(TypeLiteral.of(type));
    }

    public static <T> Matcher<TypeLiteral<?>> like(TypeLiteral<T> type) {
        return type::equals;
    }

    public static <T> Matcher<TypeLiteral<T>> is(Predicate<TypeLiteral<T>> predicate) {
        return predicate::test;
    }
}
