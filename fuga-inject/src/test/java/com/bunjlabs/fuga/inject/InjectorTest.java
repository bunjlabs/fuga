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

import com.bunjlabs.fuga.inject.bindings.InstanceBinding;
import com.bunjlabs.fuga.lang.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class InjectorTest {

    private static Injector createInjector(Unit... unit) {
        return new InjectorBuilder().withUnits(unit).build();
    }

    @Test
    void testProviderMethods() {
        var singleton = new SampleSingleton();
        var injector = createInjector(c -> c.bind(SampleSingleton.class).toInstance(singleton));

        assertSame(singleton, injector.getInstance(SampleSingleton.class));
        assertSame(singleton, injector.getProvider(SampleSingleton.class).get());
        assertTrue(injector.getBinding(SampleSingleton.class) instanceof InstanceBinding);
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
    void testInstall() {
        assertNotNull(createInjector(a -> a.install(b -> b.install(c -> c.bind(SampleA.class).auto()))).getInstance(SampleA.class));
    }

    @Test
    void testNotProvided() {
        assertThrows(ProvisionException.class,
                () -> createInjector().getInstance(SampleSingleton.class));
    }

    @Test
    void testRecursive() {
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(SampleA.class).to(SampleA.class)));
        assertThrows(ProvisionException.class,
                () -> createInjector(c -> c.bind(Loop.class).auto()).getInstance(Loop.class));
        assertThrows(ProvisionException.class,
                () -> createInjector(c -> {
                    c.bind(Loop1.class).auto();
                    c.bind(Loop2.class).auto();
                }).getInstance(Loop1.class));
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
    void testInjectorBinding() {
        assertNotNull(createInjector().getInstance(Injector.class));

        var i1 = createInjector();
        var i2 = i1.createChildInjector();
        var i3 = i2.createChildInjector();

        var i1b = i1.getInstance(Injector.class);
        var i2b = i2.getInstance(Injector.class);
        var i3b = i3.getInstance(Injector.class);

        assertSame(i1, i1b);
        assertSame(i2, i2b);
        assertSame(i3, i3b);

        assertNotSame(i1, i2b);
        assertNotSame(i1, i3b);
        assertNotSame(i2, i3b);
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
            c.bind(SampleAProvider.class).auto();
            c.bind(SampleA.class).toProvider(SampleAProvider.class);
            c.bind(SampleB.class).toProvider(SampleB::new);
            c.bind(SampleC.class).toProvider(() -> null);
        });
        assertSame(injector.getInstance(SampleSingleton.class), injector.getInstance(SampleSingleton.class));
        assertNotSame(injector.getInstance(SampleA.class), injector.getInstance(SampleA.class));
        assertNotSame(injector.getInstance(SampleB.class), injector.getInstance(SampleB.class));
        assertNull(injector.getInstance(SampleC.class));
    }

    @Test
    void testProviderComposerInternalException() {
        assertThrows(ProvisionException.class,
                () -> createInjector(c ->
                        c.bind(SampleA.class).toProvider(new SampleAExceptionProvider()))
                        .getInstance(SampleA.class));
        assertThrows(ProvisionException.class,
                () -> createInjector(c -> {
                    c.bind(SampleAExceptionProvider.class).auto();
                    c.bind(SampleA.class).toProvider(SampleAExceptionProvider.class);
                }).getInstance(SampleA.class));
        assertThrows(ProvisionException.class,
                () -> createInjector(c ->
                        c.bind(SampleA.class).toComposer(new SampleAExceptionComposer()))
                        .getInstance(SampleA.class));
        assertThrows(ProvisionException.class,
                () -> createInjector(c -> {
                    c.bind(SampleAExceptionComposer.class).auto();
                    c.bind(SampleA.class).toComposer(SampleAExceptionComposer.class);
                }).getInstance(SampleA.class));
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

            c.bind(SampleAComposer.class).auto();
            c.bind(SampleA.class).toComposer(SampleAComposer.class);

            c.bind(SampleB.class).toComposer(new Composer() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                    return (T) new SampleB();
                }
            });
            c.bind(SampleC.class).toComposer(new Composer() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                    return (T) "";
                }
            });
            c.bind(SampleD.class).toComposer(new Composer() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                    return null;
                }
            });
        });
        assertSame(injector.getInstance(SampleSingleton.class), injector.getInstance(SampleSingleton.class));
        assertNotSame(injector.getInstance(SampleA.class), injector.getInstance(SampleA.class));
        assertNotSame(injector.getInstance(SampleB.class), injector.getInstance(SampleB.class));
        assertThrows(ProvisionException.class, () -> injector.getInstance(SampleC.class));
        assertNull(injector.getInstance(SampleD.class));
    }


    @Test
    void testProvidedAndComposedBy() {
        var injector = createInjector(c -> {
            c.bind(ProvidedBySampleProvider.class).auto();
            c.bind(ComposedBySampleComposer.class).auto();

            c.bind(ProvidedBySample.class).auto();
            c.bind(ComposedBySample.class).auto();
        });

        assertNotSame(injector.getInstance(ProvidedBySample.class), injector.getInstance(ProvidedBySample.class));
        assertNotSame(injector.getInstance(ComposedBySample.class), injector.getInstance(ComposedBySample.class));
    }

    @Test
    void testSingletonScope() throws NoSuchMethodException {
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
    void testMultibindingProviderMethods() {
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

        assertTrue(injector.getAllInstances(ISampleB.class).containsAll(instances));
        assertTrue(injector.getAllProviders(ISampleB.class).stream()
                .map(Provider::get)
                .collect(Collectors.toSet())
                .containsAll(instances));
        assertTrue(injector.getAllBindings(ISampleB.class).stream()
                .allMatch(b -> b instanceof InstanceBinding));
    }

    @Test
    void testInjectAll() {
        var instances = Arrays.asList(new ISampleBImplA(), new ISampleBImplB(), new ISampleBImplC());
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
    void testInjectorAllToNonSet() {
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(InjectAllSampleErr.class).auto()));
    }

    @Test
    void testNotInjectable() {
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(NoDefaultConstructor.class).auto()));
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(TooManyInjectable.class).auto()));
    }

    @Test
    void testNonNullableDependency() {
        Unit u = c -> c.bind(NonNullableA.class).auto();

        assertThrows(ProvisionException.class,
                () -> createInjector(c -> c.bind(SampleA.class).toInstance(null))
                        .createChildInjector(u).getInstance(NonNullableA.class));
        assertThrows(ProvisionException.class,
                () -> createInjector(c -> c.bind(SampleA.class).toProvider(() -> null))
                        .createChildInjector(u).getInstance(NonNullableA.class));
        assertThrows(ProvisionException.class,
                () -> createInjector(c -> {
                    c.bind(SampleANullProvider.class).auto();
                    c.bind(SampleA.class).toProvider(SampleANullProvider.class);
                }).createChildInjector(u).getInstance(NonNullableA.class));
        assertThrows(ProvisionException.class,
                () -> createInjector(c -> c.bind(SampleA.class).toComposer(new Composer() {
                    @Override
                    public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                        return null;
                    }
                })).createChildInjector(u).getInstance(NonNullableA.class));
        assertThrows(ProvisionException.class,
                () -> createInjector(c -> {
                    c.bind(SampleANullComposer.class).auto();
                    c.bind(SampleA.class).toComposer(SampleANullComposer.class);
                }).createChildInjector(u).getInstance(NonNullableA.class));
    }

    @Test
    void testNullableDependency() {
        Unit u = c -> c.bind(NullableA.class).auto();

        assertNotNull(createInjector(c -> c.bind(SampleA.class).toInstance(null))
                .createChildInjector(u).getInstance(NullableA.class));
        assertNotNull(createInjector(c -> c.bind(SampleA.class).toProvider(() -> null))
                .createChildInjector(u).getInstance(NullableA.class));
        assertNotNull(createInjector(c -> {
            c.bind(SampleANullProvider.class).auto();
            c.bind(SampleA.class).toProvider(SampleANullProvider.class);
        }).createChildInjector(u).getInstance(NullableA.class));
        assertNotNull(createInjector(c -> c.bind(SampleA.class).toComposer(new Composer() {
            @Override
            public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                return null;
            }
        })).createChildInjector(u).getInstance(NullableA.class));
        assertNotNull(createInjector(c -> {
            c.bind(SampleANullComposer.class).auto();
            c.bind(SampleA.class).toComposer(SampleANullComposer.class);
        }).createChildInjector(u).getInstance(NullableA.class));
    }

    @Test
    void testUntargetted() {
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(SampleA.class)));
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

    public static class SampleAProvider implements Provider<SampleA> {

        @Override
        public SampleA get() {
            return new SampleA();
        }
    }

    public static class SampleANullProvider implements Provider<SampleA> {

        @Override
        public SampleA get() {
            return null;
        }
    }

    public static class SampleAExceptionProvider implements Provider<SampleA> {

        @Override
        public SampleA get() {
            throw new UnsupportedOperationException();
        }
    }

    public static class SampleAComposer implements Composer {

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
            return (T) new SampleA();
        }
    }

    public static class SampleANullComposer implements Composer {

        @Override
        public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
            return null;
        }
    }

    public static class SampleAExceptionComposer implements Composer {

        @Override
        public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
            throw new UnsupportedOperationException();
        }
    }

    @ProvidedBy(ProvidedBySampleProvider.class)
    public static class ProvidedBySample {
    }

    public static class ProvidedBySampleProvider implements Provider<ProvidedBySample> {

        @Override
        public ProvidedBySample get() {
            return new ProvidedBySample();
        }
    }

    @ComposedBy(ComposedBySampleComposer.class)
    public static class ComposedBySample {
    }

    public static class ComposedBySampleComposer implements Composer {

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
            return (T) new ComposedBySample();
        }
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

    public static class NullableA {

        @Inject
        public NullableA(@Nullable SampleA a) {
        }
    }

    public static class NonNullableA {

        @Inject
        public NonNullableA(SampleA a) {
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

    public static class Loop1 {
        @Inject
        public Loop1(Loop2 l) {
        }
    }

    public static class Loop2 {
        @Inject
        public Loop2(Loop1 l) {
        }
    }

    public static class InjectAllSample {
        Set<ISampleB> set;

        @Inject
        public InjectAllSample(@InjectAll Set<ISampleB> set) {
            this.set = set;
        }
    }

    public static class InjectAllSampleErr {
        List<ISampleB> set;

        @Inject
        public InjectAllSampleErr(@InjectAll List<ISampleB> set) {
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


    public static class NoDefaultConstructor {
        private NoDefaultConstructor() {
        }
    }

    public static class TooManyInjectable {
        @Inject
        private TooManyInjectable(int a) {
        }

        @Inject
        private TooManyInjectable(int a, int b) {
        }
    }

}
