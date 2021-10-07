package fuga.util;

import fuga.lang.FullType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static fuga.util.Matchers.*;
public class MatcherTest {

    @Test
    void basic() {
        assertTrue(any().match("String"));
        assertFalse(not(any()).match("String"));
    }

    @Test
    void logicOr() {
        var matcher = exact(Float.class).or(exact(Double.class));
        assertTrue(matcher.match(FullType.of(Float.class)));
        assertTrue(matcher.match(FullType.of(Double.class)));
        assertFalse(matcher.match(FullType.of(String.class)));
    }

    @Test
    void logicAnd() {
        var matcher = exact(Float.class).and(t -> t.getSuperType().equals(FullType.of(Number.class)));
        assertTrue(matcher.match(FullType.of(Float.class)));
        assertFalse(matcher.match(FullType.of(Double.class)));
    }
}
