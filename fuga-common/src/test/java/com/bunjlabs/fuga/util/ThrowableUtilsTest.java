package com.bunjlabs.fuga.util;

import org.junit.jupiter.api.Test;

import static com.bunjlabs.fuga.util.ThrowableUtils.getStackTraceAsString;
import static java.util.regex.Pattern.quote;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThrowableUtilsTest {

    @Test
    public void testGetStackTraceAsString() {
        class TestException extends Exception {
            TestException(String message) {
                super(message);
            }
        }
        var e = new TestException("test exception message");
        var first = quote(e.getClass().getName() + ": " + e.getMessage());
        var second = "\\s*at " + ThrowableUtilsTest.class.getName() + "\\..*";
        var rest = "(?:.*\\R?)*";

        assertTrue(getStackTraceAsString(e).matches(first + "\\R" + second + "\\R" + rest));
    }
}
