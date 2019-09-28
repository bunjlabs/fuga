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
