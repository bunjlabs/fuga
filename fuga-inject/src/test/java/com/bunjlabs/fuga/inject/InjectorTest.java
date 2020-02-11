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

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InjectorTest {

    private static Injector createInjector(Unit... unit) {
        return new InjectorBuilder().withUnits(unit).build();
    }

    @Test
    void testProviderMethods() {
        var singleton = new SampleSingleton();
        var injector = createInjector(c -> c.bind(SampleSingleton.class).toInstance(singleton));

        assertSame(singleton, injector.getInstance(Key.of(SampleSingleton.class)));
        assertSame(singleton, injector.getInstance(SampleSingleton.class));
    }

    @Test
    void testAutoInjectedInstances() {
        var injector = createInjector(c -> {
            c.bind(SampleA.class).toInstance(new SampleA());
            c.bind(FullC.class).auto();
            c.bind(ISampleAImpl.class).auto();
            c.bind(ISampleA.class).to(ISampleAImpl.class);
        });

        var ia = injector.getInstance(ISampleA.class);
        var ca = injector.getInstance(FullC.class);
        assertNotSame(ia.getCB(), ca);
        assertSame(ca.a, ca.b);
    }

    @Test
    void testNotProvided() {
        assertThrows(ProvisionException.class,
                () -> createInjector().getInstance(SampleSingleton.class));
    }

    @Test
    void testRecursive() {
        assertThrows(ProvisionException.class,
                () -> createInjector(c -> c.bind(Loop.class).auto()).getInstance(Loop.class));
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(SampleA.class).to(SampleA.class)));
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(RecursiveProvider.class).auto()));
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(RecursiveProvider.class).toProvider(RecursiveProvider.class)));
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(RecursiveComposer.class).auto()));
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(RecursiveComposer.class).toComposer(RecursiveComposer.class)));
    }

    @Test
    void testConstructor() throws NoSuchMethodException {
        final var fullConstructor = FullC.class.getConstructor(SampleA.class, SampleA.class);

        var injector = createInjector(c -> {
            c.bind(SampleA.class).toInstance(new SampleA());
            c.bind(FullC.class).toConstructor(fullConstructor);
            c.bind(ISampleAImpl.class).auto();
            c.bind(ISampleA.class).to(ISampleAImpl.class);
        });

        var c1 = injector.getInstance(FullC.class);
        var c2 = injector.getInstance(FullC.class);
        assertNotSame(c1, c2);
    }

    @Test
    void testCustomProvider() {
        var singleton = new SampleSingleton();
        var injector = createInjector(c -> {
            c.bind(SampleSingleton.class).toProvider(() -> singleton);
            c.bind(SampleA.class).toProvider(SampleA::new);
            c.bind(SampleB.class).toProvider(() -> null);
        });
        assertSame(injector.getInstance(SampleSingleton.class), injector.getInstance(SampleSingleton.class));
        assertNotSame(injector.getInstance(SampleA.class), injector.getInstance(SampleA.class));
        assertNull(injector.getInstance(SampleB.class));
    }

    @Test
    void testCustomComposer() {
        var singleton = new SampleSingleton();
        var injector = createInjector(c -> {
            c.bind(SampleSingleton.class).toComposer(new Composer() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                    return (T) singleton;
                }
            });
            c.bind(SampleA.class).toComposer(new Composer() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                    return (T) new SampleA();
                }
            });
            c.bind(SampleB.class).toComposer(new Composer() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                    return (T) "";
                }
            });
            c.bind(SampleC.class).toComposer(new Composer() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                    return null;
                }
            });
        });
        assertSame(injector.getInstance(SampleSingleton.class), injector.getInstance(SampleSingleton.class));
        assertNotSame(injector.getInstance(SampleA.class), injector.getInstance(SampleA.class));
        assertThrows(ProvisionException.class, () -> injector.getInstance(SampleB.class));
        assertThrows(ProvisionException.class, () -> injector.getInstance(SampleC.class));
    }


    @Test
    void testScopes() throws NoSuchMethodException {
        final var constructor = SampleB.class.getConstructor();
        var composer = new Composer() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                return (T) new SampleD();
            }
        };
        var injector = createInjector(c -> {
            c.bind(SampleA.class).auto().in(Singleton.class);
            c.bind(SampleB.class).toConstructor(constructor).in(Singleton.class);
            c.bind(SampleC.class).toProvider(SampleC::new).in(Singleton.class);
            c.bind(SampleD.class).toComposer(composer).in(Singleton.class);
            c.bind(SampleE.class).auto().in(Singleton.class);
            c.bind(SampleExtSingleton.class).auto();
            c.bind(SampleSingleton.class).to(SampleExtSingleton.class).in(Singleton.class);
        });
        assertSame(injector.getInstance(SampleA.class), injector.getInstance(SampleA.class));
        assertSame(injector.getInstance(SampleB.class), injector.getInstance(SampleB.class));
        assertSame(injector.getInstance(SampleC.class), injector.getInstance(SampleC.class));
        assertSame(injector.getInstance(SampleD.class), injector.getInstance(SampleD.class));
        assertSame(injector.getInstance(SampleE.class), injector.getInstance(SampleE.class));
        assertSame(injector.getInstance(SampleSingleton.class), injector.getInstance(SampleSingleton.class));
        assertNotSame(injector.getInstance(SampleExtSingleton.class), injector.getInstance(SampleExtSingleton.class));
    }

    @Test
    void testSameKey() {
        var instanceA = new ISampleBImplA();
        var instanceB = new ISampleBImplB();
        var instanceC = new ISampleBImplC();
        var injector = createInjector(c -> {
            c.bind(ISampleB.class).toInstance(instanceA);
            c.bind(ISampleB.class).toInstance(instanceB);
            c.bind(ISampleB.class).toInstance(instanceC);
        });

        assertSame(injector.getInstance(ISampleB.class), instanceC);
    }

    @Test
    void testMultibinding() {
        var instances = Arrays.asList(
                new ISampleBImplA(),
                new ISampleBImplB(),
                new ISampleBImplC()
        );
        var injector = createInjector(c -> {
            c.bind(ISampleB.class).toInstance(instances.get(0));
            c.bind(ISampleB.class).toInstance(instances.get(1));
            c.bind(ISampleB.class).toInstance(instances.get(2));
        });

        var injectedInstances = injector.getAllInstances(ISampleB.class);

        assertTrue(injectedInstances.containsAll(instances));
    }

    @Test
    void testInjectorAll() {
        var instances = Arrays.asList(
                new ISampleBImplA(),
                new ISampleBImplB(),
                new ISampleBImplC()
        );
        var injector = createInjector(c -> {
            c.bind(ISampleB.class).toInstance(instances.get(0));
            c.bind(ISampleB.class).toInstance(instances.get(1));
            c.bind(ISampleB.class).toInstance(instances.get(2));

            c.bind(InjectAllSample.class).auto();
        });

        var injectAllSample = injector.getInstance(InjectAllSample.class);

        assertTrue(injectAllSample.set.containsAll(instances));
    }

    @Test
    void testErrors() {
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(SampleA.class).to(SampleA.class)));
    }


    public interface ISampleA {
        FullC getCB();
    }

    public interface ISampleB {
    }

    public static class SampleSingleton {
    }

    public static class SampleExtSingleton extends SampleSingleton {
    }

    public static class SampleA {
    }

    public static class SampleB {
    }

    public static class SampleC {
    }

    public static class SampleD {
    }

    public static class SampleE {
    }

    public static class FullC {

        SampleA a;
        SampleA b;

        @Inject
        public FullC(SampleA a, SampleA b) {
            this.a = a;
            this.b = b;
        }
    }


    public static class ISampleAImpl implements ISampleA {

        FullC ca;

        @Inject
        public ISampleAImpl(FullC ca) {
            this.ca = ca;
        }

        @Override
        public FullC getCB() {
            return ca;
        }
    }

    public static class ISampleBImplA implements ISampleB {
    }
    public static class ISampleBImplB implements ISampleB {
    }
    public static class ISampleBImplC implements ISampleB {
    }

    public static class Loop {

        @Inject
        public Loop(Loop l) {
        }
    }


    public static class InjectAllSample {
        Set<ISampleB> set;

        @Inject
        public InjectAllSample(@InjectAll Set<ISampleB> set) {
            this.set = set;
        }
    }
    @ProvidedBy(RecursiveProvider.class)
    public static class RecursiveProvider implements Provider<RecursiveProvider> {

        @Override
        public RecursiveProvider get() {
            return null;
        }
    }

    @ComposedBy(RecursiveComposer.class)
    public static class RecursiveComposer implements Composer {

        @Override
        public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
            return null;
        }
    }

}
