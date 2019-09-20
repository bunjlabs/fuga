package com.bunjlabs.fuga.inject;

import com.bunjlabs.fuga.inject.support.InjectorBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InjectorTest {

    private static Injector createInjector(Unit... unit) {
        return new InjectorBuilder().withUnits(unit).build();
    }

    @Test
    public void testProviderMethods() {
        var singleton = new SampleSingleton();
        var injector = createInjector(c -> c.bind(SampleSingleton.class).toInstance(singleton));

        assertSame(singleton, injector.getInstance(Key.of(SampleSingleton.class)));
        assertSame(singleton, injector.getInstance(SampleSingleton.class));
    }

    @Test
    public void testAutoInjectedInstances() {
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
    public void testNotProvided() {
        assertThrows(ProvisionException.class, () -> createInjector().getInstance(SampleSingleton.class));
    }

    @Test
    public void testCircular() {
        var injector = createInjector(c -> c.bind(Loop.class).auto());
        assertThrows(ProvisionException.class, () -> injector.getInstance(Loop.class));
    }

    @Test
    public void testCustomProvider() {
        var singleton = new SampleSingleton();
        var injector = createInjector(c -> {
            c.bind(SampleSingleton.class).toProvider(() -> singleton);
            c.bind(SampleA.class).toProvider(SampleA::new);
            c.bind(SampleB.class).toProvider(() -> null);
        });
        assertSame(injector.getInstance(SampleSingleton.class), injector.getInstance(SampleSingleton.class));
        assertNotSame(injector.getInstance(SampleA.class), injector.getInstance(SampleA.class));
        assertThrows(ProvisionException.class, () -> injector.getInstance(SampleB.class));
    }

    @Test
    public void testCustomComposer() {
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

}
