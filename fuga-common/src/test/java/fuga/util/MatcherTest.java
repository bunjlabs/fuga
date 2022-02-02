package fuga.util;

import fuga.lang.TypeLiteral;
import org.junit.jupiter.api.Test;

import static fuga.util.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatcherTest {

    @Test
    void basic() {
        assertTrue(any().match("String"));
        assertFalse(not(any()).match("String"));
    }

    @Test
    void logicOr() {
        var matcher = is(TypeLiteral.of(Float.class)::equals).or(TypeLiteral.of(Double.class)::equals);
        //assertTrue(matcher.match(FullType.of(Float.T)));
        //assertTrue(matcher.match(FullType.of(Double.class)));
        //assertFalse(matcher.match(FullType.of(String.class)));
    }

    @Test
    void logicAnd() {
        var matcher = only(Float.class).and(t -> t.getSuperType().equals(TypeLiteral.of(Number.class)));
        assertTrue(matcher.match(TypeLiteral.of(Float.class)));
        //assertFalse(matcher.match(FullType.of(Double.class)));
    }
}
