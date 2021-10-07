package fuga.inject.support;

import fuga.inject.MatchedWatching;
import fuga.lang.FullType;
import fuga.util.Matcher;

abstract class AbstractMatchedWatching implements MatchedWatching {

    private final Matcher<FullType<?>> matcher;
    private final Interceptor interceptor;

    AbstractMatchedWatching(Matcher<FullType<?>> matcher) {
        this.matcher = matcher;
        this.interceptor = null;
    }

    AbstractMatchedWatching(Matcher<FullType<?>> matcher, Interceptor interceptor) {
        this.matcher = matcher;
        this.interceptor = interceptor;
    }

    @Override
    public Matcher<FullType<?>> getMatcher() {
        return matcher;
    }

    Interceptor getInterceptor() {
        return interceptor;
    }
}
