package fuga.inject;

import fuga.lang.FullType;
import fuga.util.Matcher;

public interface MatchedWatching {

    Matcher<FullType<?>> getMatcher();

    <V> V acceptVisitor(MatchedWatchingVisitor<V> visitor);
}
