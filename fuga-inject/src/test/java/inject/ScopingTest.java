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
import fuga.inject.*;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.*;

class ScopingTest {

    @Test
    void testSingletonAnnotation() {
        var injector = Injector.create(c -> c.bind(Sample.class).in(Singleton.class));
        assertSame(injector.getInstance(Sample.class), injector.getInstance(Sample.class));
    }

    @Test
    void testSingletonKey() {
        var injector = Injector.create(c -> c.bind(Sample.class).inScope(SingletonScope.class));
        assertSame(injector.getInstance(Sample.class), injector.getInstance(Sample.class));
    }

    @Test
    void testCustomScopes() {
        var scope = new DummyScope();
        assertNotNull(Injector.create(c -> c.bind(DummyScope.class).toInstance(scope))
                .createChildInjector(c -> c.bind(Sample.class).in(Dummy.class))
                .getInstance(Sample.class));
        assertTrue(scope.scoped);

        assertThrows(ProvisionException.class, () -> Injector.create(c -> c.bind(NullProviderScope.class).toInstance(new NullProviderScope()))
                .createChildInjector(c -> c.bind(Sample.class).in(Null.class))
                .getInstance(Sample.class));

        assertThrows(ProvisionException.class, () -> Injector.create(c -> c.bind(Sample.class).inScope(new Scope() {
            @Override
            public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
                return () -> null;
            }
        })).getInstance(Sample.class));
    }

    @Test
    void testNullScope() {
        var injector = Injector.create(c -> c.bind(NullScope.class).toInstance(new NullScope()))
                .createChildInjector(c -> c.bind(Sample.class).inScope(NullScope.class));
        assertNotSame(injector.getInstance(Sample.class), injector.getInstance(Sample.class));
    }

    public static class Sample {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @ScopeAnnotation(scope = DummyScope.class)
    @interface Dummy {
    }


    @Retention(RetentionPolicy.RUNTIME)
    @ScopeAnnotation(scope = NullProviderScope.class)
    @interface Null {
    }

    @ScopeAnnotation
    @interface Err1CustomScopeAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Err2CustomScopeAnnotation {
    }

    static class NullScope implements Scope {

        @Override
        public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
            return null;
        }
    }

    static class NullProviderScope implements Scope {

        @Override
        public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
            return () -> null;
        }
    }

    static class DummyScope implements Scope {
        boolean scoped = false;

        @Override
        public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
            scoped = true;
            return provider;
        }
    }
}
