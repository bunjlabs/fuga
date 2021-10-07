package fuga.inject;

import fuga.common.Key;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeyedWatchTest {

    @Test
    void basic() {
        var listener = new SampleClass1.Listener();

        var injector = Injector.create(c -> {
            c.watch(SampleClass1.class).with(listener);
            c.bind(SampleClass1.class);
        });

        injector.getInstance(SampleClass1.class);
        assertTrue(listener.provisioned);
    }

    @Test
    void delegatedToInstance() {
        var listener = new SampleClass1.Listener();

        var injector = Injector.create(c -> {
            c.watch(SampleClass1.class).with(SampleClass1.Listener.class);

            c.bind(SampleClass1.Listener.class).toInstance(listener);
            c.bind(SampleClass1.class);
        });

        injector.getInstance(SampleClass1.class);
        assertTrue(listener.provisioned);
    }

    @Test
    void delegatedToProvider() {
        var listener = new SampleClass1.Listener();

        var injector = Injector.create(c -> {
            c.watch(SampleClass1.class).with(SampleClass1.Listener.class);

            c.bind(SampleClass1.Listener.class).toProvider(() -> listener);
            c.bind(SampleClass1.class);
        });

        injector.getInstance(SampleClass1.class);
        assertTrue(listener.provisioned);
    }

    @Test
    void child() {
        var listener1 = new SampleClass1.Listener();
        var listener2 = new SampleClass2.Listener();

        var injector = Injector.create(c -> {
            c.watch(SampleClass1.class).with(listener1);

            c.bind(SampleClass2.class);
        });

        var childInjector = injector.createChildInjector(c -> {
            c.watch(SampleClass2.class).with(listener2);

            c.bind(SampleClass1.class);
        });

        childInjector.getInstance(SampleClass1.class);
        assertTrue(listener1.provisioned);

        childInjector.getInstance(SampleClass2.class);
        assertFalse(listener2.provisioned);
    }

    public static class SampleClass1 {
        public static class Listener extends KeyedWatchTest.Listener<SampleClass1> {

        }
    }

    public static class SampleClass2 {
        public static class Listener extends KeyedWatchTest.Listener<SampleClass2> {

        }
    }

    public static class Listener<T> implements TypeListener<T> {
        boolean provisioned = false;

        @Override
        public void onProvision(Key<T> key, T instance) {
            provisioned = true;
        }

        void reset() {
            provisioned = false;
        }
    }
}
