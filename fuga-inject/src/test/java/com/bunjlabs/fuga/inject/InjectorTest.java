package com.bunjlabs.fuga.inject;

import org.junit.jupiter.api.Test;

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
                    return (T) new String();
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
    void testScopes() {
        var composer = new Composer() {
            @Override
            public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                return (T) new SampleC();
            }
        };
        var injector = createInjector(c -> {
            c.bind(SampleA.class).auto().in(Singleton.class);
            c.bind(SampleB.class).toProvider(SampleB::new).in(Singleton.class);
            c.bind(SampleC.class).toComposer(composer).in(Singleton.class);
        });
        assertSame(injector.getInstance(SampleA.class), injector.getInstance(SampleA.class));
        assertSame(injector.getInstance(SampleB.class), injector.getInstance(SampleB.class));
        assertSame(injector.getInstance(SampleC.class), injector.getInstance(SampleC.class));
    }


    @Test
    void testErrors() {
        assertThrows(ConfigurationException.class,
                () -> createInjector(c -> c.bind(SampleA.class).to(SampleA.class)));
    }


    public interface ISampleA {

        FullC getCB();
    }

    public static class SampleSingleton {

    }

    public static class SampleA {

    }

    public static class SampleB {

    }

    public static class SampleC {

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

    public static class Loop {

        @Inject
        public Loop(Loop l) {
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
