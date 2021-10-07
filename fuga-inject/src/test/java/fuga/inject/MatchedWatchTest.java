package fuga.inject;

import fuga.common.Key;
import fuga.util.Matchers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchedWatchTest {
    @Test
    void basic() {
        var listener = new Listener();

        var injector = Injector.create(c -> {
            c.watch(Matchers.any()).with(listener);
            c.bind(SampleClass1.class);
        });

        injector.getInstance(SampleClass1.class);
        assertTrue(listener.provisioned);
    }

    @Test
    void delegatedToInstance() {
        var listener = new Listener();

        var injector = Injector.create(c -> {
            c.watch(Matchers.any()).with(Listener.class);
            c.bind(Listener.class).toInstance(listener);
            c.bind(SampleClass1.class);
        });

        injector.getInstance(SampleClass1.class);
        assertTrue(listener.provisioned);
    }

    @Test
    void delegatedToProvider() {
        var listener = new Listener();

        var injector = Injector.create(c -> {
            c.watch(Matchers.any()).with(Listener.class);
            c.bind(Listener.class).toProvider(() -> listener);
            c.bind(SampleClass1.class);
        });

        injector.getInstance(SampleClass1.class);
        assertTrue(listener.provisioned);
    }

    @Test
    void matched() {
        var listener1 = new Listener();
        var listener2 = new Listener();

        var injector = Injector.create(c -> {
            c.watch(Matchers.exact(SampleClass1.class)).with(listener1);
            c.watch(Matchers.exact(SampleClass2.class)).with(listener2);

            c.bind(SampleClass1.class);
            c.bind(SampleClass2.class);
        });

        injector.getInstance(SampleClass1.class);
        assertTrue(listener1.provisioned && !listener2.provisioned);

        listener1.reset();
        listener2.reset();

        injector.getInstance(SampleClass2.class);
        assertTrue(listener2.provisioned && !listener1.provisioned);
    }

    @Test
    void child() {
        var listener1 = new Listener();
        var listener2 = new Listener();

        var injector = Injector.create(c -> {
            c.watch(Matchers.exact(SampleClass1.class)).with(listener1);

            c.bind(SampleClass2.class);
        });

        var childInjector = injector.createChildInjector(c -> {
            c.watch(Matchers.exact(SampleClass2.class)).with(listener2);

            c.bind(SampleClass1.class);
        });

        childInjector.getInstance(SampleClass1.class);
        assertTrue(listener1.provisioned);

        childInjector.getInstance(SampleClass2.class);
        assertFalse(listener2.provisioned);
    }

    public static class SampleClass1 {

    }

    public static class SampleClass2 {

    }

    public static class Listener implements ProvisionListener {
        boolean provisioned = false;

        @Override
        public <T> void onProvision(Key<T> key, T instance) {
            provisioned = true;
        }

        void reset() {
            provisioned = false;
        }
    }
}
