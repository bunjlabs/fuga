package fuga.inject.support;

import fuga.lang.TypeLiteral;
import fuga.util.Matcher;

abstract class BindingMatcher<T> {
    private final Matcher<? super TypeLiteral<T>> matcher;

    BindingMatcher(Matcher<? super TypeLiteral<T>> matcher) {
        this.matcher = matcher;
    }

    Matcher<? super TypeLiteral<T>> getMatcher() {
        return matcher;
    }
}
