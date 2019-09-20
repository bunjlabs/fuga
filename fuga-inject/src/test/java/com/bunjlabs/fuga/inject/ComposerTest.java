package com.bunjlabs.fuga.inject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class ComposerTest {

    private static Injector createInjector(Unit... unit) {
        return new InjectorBuilder().withUnits(unit).build();
    }

    @Test
    public void testComposerRequester() {
        createInjector(c -> c.bind(SampleC.class).toComposer(new Composer() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                assertSame(Injector.class, requester.getType());
                return (T) new SampleC();
            }
        })).getInstance(SampleC.class);

        Injector injector = createInjector(c -> {
            c.bind(SampleA.class).auto();
            c.bind(SampleB.class).auto();
            c.bind(SampleC.class).toComposer(new Composer() {
                @Override
                @SuppressWarnings("unchecked")
                public <T> T get(Key<?> requester, Key<T> requested) throws ProvisionException {
                    assertSame(SampleB.class, requester.getType());
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
