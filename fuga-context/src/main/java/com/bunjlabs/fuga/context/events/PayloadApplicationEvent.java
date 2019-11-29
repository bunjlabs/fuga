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

package com.bunjlabs.fuga.context.events;

import com.bunjlabs.fuga.context.ApplicationEvent;
import com.bunjlabs.fuga.util.Assert;

public class PayloadApplicationEvent<T> extends ApplicationEvent {

    private final T payload;

    public PayloadApplicationEvent(Object source, T payload) {
        super(source);
        this.payload = Assert.notNull(payload, "Payload must not be null");
    }

    public T getPayload() {
        return this.payload;
    }
}
