/*
 * Copyright 2019 Bunjlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
