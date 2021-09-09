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

package fuga.context.support;

import fuga.context.ApplicationEvent;
import fuga.context.ApplicationEventDispatcher;
import fuga.context.ApplicationEventPublisher;
import fuga.context.events.PayloadApplicationEvent;
import fuga.util.Assert;

public class DefaultApplicationEventPublisher implements ApplicationEventPublisher {
    private final ApplicationEventDispatcher eventDispatcher;

    public DefaultApplicationEventPublisher(ApplicationEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void publish(Object event) {
        Assert.notNull(event, "Event must not be null");

        ApplicationEvent applicationEvent;
        if (event instanceof ApplicationEvent) {
            applicationEvent = (ApplicationEvent) event;
        } else {
            applicationEvent = new PayloadApplicationEvent<>(this, event);
        }

        eventDispatcher.dispatch(applicationEvent);
    }
}
