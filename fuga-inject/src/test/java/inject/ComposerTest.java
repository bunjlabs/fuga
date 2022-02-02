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

package inject;

import fuga.common.Key;
import fuga.inject.Composer;
import fuga.inject.Inject;
import fuga.inject.Injector;
import fuga.inject.ProvisionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class ComposerTest {

    @Test
    public void testComposerRequested() {
        Injector.create(c -> c.bind(SampleC.class).toComposer(new Composer() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                assertSame(SampleC.class, requested.getRawType());
                return (T) new SampleC();
            }
        })).getInstance(SampleC.class);
    }

    @Test
    public void testComposerRequester() {
        Injector.create(c -> c.bind(SampleC.class).toComposer(new Composer() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                assertSame(Injector.class, requester.getRawType());
                return (T) new SampleC();
            }
        })).getInstance(SampleC.class);

        Injector injector = Injector.create(c -> {
            c.bind(SampleA.class);
            c.bind(SampleB.class);
            c.bind(SampleC.class).toComposer(new Composer() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                    assertSame(SampleB.class, requester.getRawType());
                    return (T) new SampleC();
                }
            });

        });

        injector.getInstance(SampleA.class);
    }

    public static class SampleA {
        @Inject
        public SampleA(SampleB b) {
        }
    }

    public static class SampleB {
        @Inject
        public SampleB(SampleC b) {
        }
    }

    public static class SampleC {

    }
}
