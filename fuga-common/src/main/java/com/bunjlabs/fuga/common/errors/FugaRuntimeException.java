package com.bunjlabs.fuga.common.errors;

import com.bunjlabs.fuga.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FugaRuntimeException extends RuntimeException {

    private final Collection<ErrorMessage> errorMessages;

    public FugaRuntimeException(String message) {
        this(Collections.singletonList(new ErrorMessage(message)));
    }

    public FugaRuntimeException(String message, Throwable cause) {
        this(Collections.singletonList(new ErrorMessage(cause, message)));
    }

    public FugaRuntimeException(Iterable<ErrorMessage> messages) {
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
