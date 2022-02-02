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

package fuga.logging.support;

import fuga.common.Key;
import fuga.inject.ProvisionException;
import fuga.logging.LoggerComposer;
import org.slf4j.LoggerFactory;

public class DefaultLoggerComposer implements LoggerComposer {
    @Override
    public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
        if (java.util.logging.Logger.class.equals(requested.getRawType())) {
            return doJavaGet(requester.getRawType(), requested.getRawType());
        }

        if (org.slf4j.Logger.class.equals(requested.getRawType())) {
            return doSlf4jGet(requester.getRawType(), requested.getRawType());
        }

        throw new ProvisionException("This composer can create org.slf4j.Logger or java.util.logging.Logger only");
    }

    @SuppressWarnings("unchecked")
    private <T> T doSlf4jGet(Class<?> requester, Class<T> cast) {
        return (T) LoggerFactory.getLogger(requester);
    }

    @SuppressWarnings("unchecked")
    private <T> T doJavaGet(Class<?> requester, Class<T> cast) {
        return (T) java.util.logging.Logger.getLogger(requester.getName());
    }
}
