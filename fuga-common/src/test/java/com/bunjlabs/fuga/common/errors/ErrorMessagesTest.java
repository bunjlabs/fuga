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

package com.bunjlabs.fuga.common.errors;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ErrorMessagesTest {

    @Test
    public void testCommon() {
        var errors = new ArrayList<ErrorMessage>();

        errors.add(new ErrorMessage("Error in %s with asdasd %s", "ABC", 123));
        errors.add(new ErrorMessage(new NullPointerException(), "Null pointer! in %s with asdasd %s", "ABC", 123));
        errors.add(new ErrorMessage(new NullPointerException(), Arrays.asList(ErrorMessagesTest.class, String.class, "asd"), "Null pointer! in %s with asdasd %s", "ABC", 123));

        assertFalse(ErrorMessages.formatMessages(errors).isEmpty());
    }
}
