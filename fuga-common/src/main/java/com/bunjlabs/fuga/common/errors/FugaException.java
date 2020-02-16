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
