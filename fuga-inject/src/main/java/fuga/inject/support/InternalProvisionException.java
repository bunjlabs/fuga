/*
 * Copyright 2019-2021 Bunjlabs
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

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fuga.inject.support;

import fuga.common.errors.ErrorMessage;
import fuga.inject.Dependency;
import fuga.inject.ProvisionException;
import fuga.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

class InternalProvisionException extends Exception {

    private final Collection<ErrorMessage> errorMessages;

    private InternalProvisionException(ErrorMessage errorMessage) {
        this(Collections.singletonList(errorMessage));
    }

    private InternalProvisionException(Iterable<ErrorMessage> messages) {
        var mutableErrorMessages = new ArrayList<ErrorMessage>();
        messages.forEach(mutableErrorMessages::add);
        this.errorMessages = Collections.unmodifiableCollection(mutableErrorMessages);
        Assert.isFalse(this.errorMessages.isEmpty(), "Can't create provision exception with no error message");
    }

    private static InternalProvisionException create(String format, Object... arguments) {
        return new InternalProvisionException(new ErrorMessage(format, arguments));
    }

    private static InternalProvisionException create(Throwable cause, String format, Object... arguments) {
        return new InternalProvisionException(new ErrorMessage(cause, format, arguments));
    }

    static InternalProvisionException nullInjectedIntoNonNullableDependency(Object source, Dependency<?> dependency) {
        return create("Null returned by binding at %s%n but %s is not @Nullable", source, dependency);
    }

    static InternalProvisionException errorInProvider(Throwable cause) {
        return create(cause, "Error in custom provider, %s", cause);
    }

    static InternalProvisionException errorInComposer(Throwable cause) {
        return create(cause, "Error in custom composer, %s", cause);
    }

    static InternalProvisionException errorInjectingConstructor(Throwable cause) {
        return create(cause, "Error injecting constructor, %s", cause);
    }

    static InternalProvisionException errorInProvisionListener(Throwable cause) {
        return create(cause, "Error in provision listener, %s", cause);
    }

    static InternalProvisionException nullProvisionListener(Object source) {
        return create("Got null provision listener while provision %s", source);
    }

    static InternalProvisionException circularDependencies(Class<?> expectedType) {
        return create("Found a circular dependency involving %s.", expectedType);
    }

    static InternalProvisionException sourceDependencyHasUnexpectedType(Object expected, Object source) {
        return create("Unexpected type %s returned for source dependency %s.", source, expected);
    }

    ProvisionException toProvisionException() {
        return new ProvisionException(errorMessages);
    }
}
