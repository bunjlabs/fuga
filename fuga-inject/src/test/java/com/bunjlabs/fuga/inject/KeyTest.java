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

package com.bunjlabs.fuga.inject;

import com.bunjlabs.fuga.util.FullType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;


class KeyTest {

    @Test
    void testKeyEquality() {
        var a = Key.of(Foo.class);
        var b = Key.of((Type) Foo.class);
        var c = Key.of(FullType.of(Foo.class));

        assertEquals(a, b);
        assertEquals(b, c);
        assertEquals(a, c);
    }


    interface Foo {
    }
}
