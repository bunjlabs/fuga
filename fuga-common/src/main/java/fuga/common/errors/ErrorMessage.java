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

package fuga.common.errors;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ErrorMessage {

    private final List<Object> sources;
    private final String message;
    private final Throwable cause;


    public ErrorMessage(String messageFormat, Object... arguments) {
        this(null, messageFormat, arguments);
    }

    public ErrorMessage(Throwable cause, String messageFormat, Object... arguments) {
        this(cause, Collections.emptyList(), messageFormat, arguments);
    }

    public ErrorMessage(Throwable cause, List<Object> sources, String messageFormat, Object... arguments) {
        String message = String.format(messageFormat, arguments);
        this.sources = Collections.unmodifiableList(sources);
        this.message = message;
        this.cause = cause;
    }

    public List<Object> getSources() {
        return sources;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getCause() {
        return cause;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, cause, sources);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorMessage that = (ErrorMessage) o;
        return sources.equals(that.sources) &&
                message.equals(that.message) &&
                cause.equals(that.cause);
    }
}
