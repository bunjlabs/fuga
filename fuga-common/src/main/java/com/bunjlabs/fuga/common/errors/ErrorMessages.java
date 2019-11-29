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

package com.bunjlabs.fuga.common.errors;

import com.bunjlabs.fuga.util.ThrowableUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ErrorMessages {

    private final String NO_SOURCE = "[[no source]]";

    private final ErrorMessages root;
    private final ErrorMessages parent;

    private final Object source;
    private List<ErrorMessage> errors;

    public ErrorMessages() {
        this.root = this;
        this.parent = null;
        this.source = NO_SOURCE;
    }

    public ErrorMessages(Object source) {
        this.root = this;
        this.parent = null;
        this.source = source;
    }

    private ErrorMessages(ErrorMessages parent, Object source) {
        this.root = parent.root;
        this.parent = parent;
        this.source = source;
    }

    public static Throwable getOnlyCause(Collection<ErrorMessage> messages) {
        var causes = messages.stream()
                .map(ErrorMessage::getCause)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
        if (causes.size() == 1) return causes.get(0);
        return null;
    }

    public static String formatMessages(Collection<ErrorMessage> errorMessages) {
        return formatMessages("Error occurred", errorMessages);
    }

    public static String formatMessages(String heading, Collection<ErrorMessage> errorMessages) {
        var fmt = new Formatter().format(heading);

        if (errorMessages.size() == 1) {
            fmt.format(" (1 error):%n%n");
        } else {
            fmt.format(" (%s error):%n%n", errorMessages.size());
        }

        int index = 1;
        for (var m : errorMessages) {
            fmt.format("%s) %s%n", index++, m.getMessage());

            var sources = m.getSources();
            for (var source : sources) {
                fmt.format("  at %s%n", source);
            }

            var cause = m.getCause();
            if (cause != null) {
                fmt.format("Caused by: %s%n", ThrowableUtils.getStackTraceAsString(m.getCause()));
            }
        }

        return fmt.toString();
    }

    public ErrorMessages withSource(Object source) {
        return source == this.source || source == NO_SOURCE ? this : new ErrorMessages(this, source);
    }

    private List<Object> getSources() {
        var sources = new ArrayList<>();
        for (var e = this; e != null; e = e.parent) {
            if (e.source != NO_SOURCE) {
                sources.add(0, e.source);
            }
        }
        return sources;
    }

    public boolean hasErrors() {
        return root.errors != null;
    }

    public ErrorMessages addMessage(String messageFormat, Object... arguments) {
        return addMessage(null, messageFormat, arguments);
    }

    public ErrorMessages addMessage(Throwable cause, String messageFormat, Object... arguments) {
        addMessage(new ErrorMessage(cause, getSources(), messageFormat, arguments));
        return this;
    }

    public ErrorMessages addMessage(ErrorMessage message) {
        if (root.errors == null) {
            root.errors = new ArrayList<>();
        }
        root.errors.add(message);
        return this;
    }

    public List<ErrorMessage> getMessages() {
        if (root.errors == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(root.errors);
    }

    public String formatMessages() {
        return formatMessages(getMessages());
    }

    public String formatMessages(String heading) {
        return formatMessages(heading, getMessages());
    }
}
