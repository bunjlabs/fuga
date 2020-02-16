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

package com.bunjlabs.fuga.inject;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.*;

class ScopeInjectorTest {
    private static Injector createInjector(Unit... unit) {
        return new InjectorBuilder().withUnits(unit).build();
    }

    @Test
    void testCustomScopes() {
        assertNotNull(createInjector(c ->
                c.bindScope(CustomScopeAnnotation.class, new DummyScope()))
                .createChildInjector(c -> c.bind(Sample.class).auto().in(CustomScopeAnnotation.class))
                .getInstance(Sample.class));

        assertNull(createInjector(c ->
                c.bindScope(CustomScopeAnnotation.class, new NullProviderScope()))
                .createChildInjector(c -> c.bind(Sample.class).auto().in(CustomScopeAnnotation.class))
                .getInstance(Sample.class));

        assertNull(createInjector(c -> c.bind(Sample.class).auto().in(new Scope() {
            @Override
            public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
                return () -> null;
            }
        })).getInstance(Sample.class));
    }

    @Test
    void testIncorrectDefinedCustomScopes() {
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bindScope(Err1CustomScopeAnnotation.class, new DummyScope())));
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bindScope(Err2CustomScopeAnnotation.class, new DummyScope())));
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(Sample.class).auto().in(CustomScopeAnnotation.class)));
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bindScope(CustomScopeAnnotation.class, new NullScope()))
                        .createChildInjector(c -> c.bind(Sample.class).auto().in(CustomScopeAnnotation.class)));
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bindScope(CustomScopeAnnotation.class, null)));
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(Sample.class).auto().in((Scope) null)));
    }

    public static class Sample {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @ScopeAnnotation
    @interface CustomScopeAnnotation {
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

        @Override
        public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
            return provider;
        }
    }
}
