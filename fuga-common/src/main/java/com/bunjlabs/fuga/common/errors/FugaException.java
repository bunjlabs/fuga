package com.bunjlabs.fuga.common.errors;

import com.bunjlabs.fuga.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FugaException extends Exception {
    private final Collection<ErrorMessage> errorMessages;

    public FugaException(String message) {
        this(Collections.singletonList(new ErrorMessage(message)));
    }

    public FugaException(String message, Throwable cause) {
        this(Collections.singletonList(new ErrorMessage(cause, message)));
    }

    public FugaException(Iterable<ErrorMessage> messages) {
        var mutableErrorMessages = new ArrayList<ErrorMessage>();
        messages.forEach(mutableErrorMessages::add);
        this.errorMessages = Collections.unmodifiableCollection(mutableErrorMessages);
        Assert.isFalse(this.errorMessages.isEmpty(), "Can't create fuga exception with no error message");
        initCause(ErrorMessages.getOnlyCause(this.errorMessages));
    }

    @Override
    public String getMessage() {
        return ErrorMessages.formatMessages("Error occurred", errorMessages);
    }
}
