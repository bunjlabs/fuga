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

package com.bunjlabs.fuga.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AssertTest {

    @Test
    public void testNotNull() {
        Assert.notNull("");
        Assert.notNull("", "msg");
        assertThrows(IllegalArgumentException.class, () -> Assert.notNull(null));
        assertThrows(IllegalArgumentException.class, () -> Assert.notNull(null, "msg"));
    }

    @Test
    public void testIsFalse() {
        Assert.isFalse(false);
        Assert.isFalse(false, "msg");
        assertThrows(IllegalArgumentException.class, () -> Assert.isFalse(true));
        assertThrows(IllegalArgumentException.class, () -> Assert.isFalse(true, "msg"));
    }

    @Test
    public void testIsTrue() {
        Assert.isTrue(true);
        Assert.isTrue(true, "msg");
        assertThrows(IllegalArgumentException.class, () -> Assert.isTrue(false));
        assertThrows(IllegalArgumentException.class, () -> Assert.isTrue(false, "msg"));
    }

    @Test
    public void testHasText() {
        Assert.hasText("text");
        Assert.hasText("text", "msg");
        assertThrows(IllegalArgumentException.class, () -> Assert.hasText(""));
        assertThrows(IllegalArgumentException.class, () -> Assert.hasText("", "msg"));
    }

    @Test
    public void testNotEmpty() {
        Assert.notEmpty(new Object[]{1});
        Assert.notEmpty(new Object[]{1}, "msg");
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty(new Object[]{}));
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty(new Object[]{}, "msg"));
    }

    @Test
    public void testIsEmpty() {
        Assert.isEmpty(new Object[]{});
        Assert.isEmpty(new Object[]{}, "msg");
        assertThrows(IllegalArgumentException.class, () -> Assert.isEmpty(new Object[]{1}));
        assertThrows(IllegalArgumentException.class, () -> Assert.isEmpty(new Object[]{1}, "msg"));
    }
}
