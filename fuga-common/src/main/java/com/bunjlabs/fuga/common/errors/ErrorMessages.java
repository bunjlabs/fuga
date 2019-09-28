package com.bunjlabs.fuga.common.errors;

import com.bunjlabs.fuga.util.ThrowableUtils;

import java.util.Collection;
import java.util.Formatter;
import java.util.Objects;
import java.util.stream.Collectors;

public class ErrorMessages {

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
                fmt.format("Caused by: %s", ThrowableUtils.getStackTraceAsString(m.getCause()));
            }

            fmt.format("%n");
        }

        return fmt.toString();
    }
}
