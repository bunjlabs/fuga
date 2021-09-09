/*
 * Copyright 2019-2020 Bunjlabs
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

package fuga.util;

import org.junit.jupiter.api.Test;

import static fuga.util.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class ObjectUtilsTest {

    @Test
    public void testIdentityStrings() {
        assertEquals("", identityToString(null));
        assertFalse(() -> identityToString(new Object()).isEmpty());
        assertEquals("0", getIdentityHexString(null));
        assertFalse(() -> getIdentityHexString(new Object()).isEmpty());
    }

    @Test
    public void testToStringJoiner() {
        Object o = null;
        Class c = null;
        String s = "";
        assertThrows(IllegalArgumentException.class, () -> toStringJoiner(o));
        assertThrows(IllegalArgumentException.class, () -> toStringJoiner(c));
        assertThrows(IllegalArgumentException.class, () -> toStringJoiner(s));
        assertEquals("Object[]", toStringJoiner(Object.class).toString());
        assertEquals("Object[]", toStringJoiner(new Object()).toString());
        assertEquals("Object[]", toStringJoiner("Object").toString());
        assertEquals("Object[a=1]", toStringJoiner(Object.class).add("a", 1).toString());
        assertEquals("Object[a=1]", toStringJoiner(Object.class).add("a", 1L).toString());
        assertEquals("Object[a=1.0]", toStringJoiner(Object.class).add("a", 1f).toString());
        assertEquals("Object[a=1.0]", toStringJoiner(Object.class).add("a", 1d).toString());
        assertEquals("Object[a=c]", toStringJoiner(Object.class).add("a", 'c').toString());
        assertEquals("Object[a=true]", toStringJoiner(Object.class).add("a", true).toString());
        assertEquals("Object[a=test]", toStringJoiner(Object.class).add("a", new Sample()).toString());
        assertEquals("Object[a=1, b=2.0, c=true]",
                toStringJoiner(Object.class)
                        .add("a", 1)
                        .add("b", 2f)
                        .add("c", true)
                        .toString());
    }

    static class Sample {
        @Override
        public String toString() {
            return "test";
        }
    }
}
