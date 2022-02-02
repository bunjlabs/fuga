package inject;

import fuga.inject.*;
import org.junit.jupiter.api.Test;

import static fuga.util.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class MatchTest {

    @Test
    void testAny() {
        var listener = new Listener<>();

        var i = Injector.create(c -> {
            c.bind(SampleA.class);
            c.bind(SampleB.class);
            c.match(any()).watch(listener);
        });

        var a = i.getInstance(SampleA.class);
        assertSame(a, listener.instance);

        var b = i.getInstance(SampleB.class);
        assertSame(b, listener.instance);

        assertEquals(2, listener.provisioned);
    }

    @Test
    void testOnly() {
        var listener = new Listener<SampleA>();

        var i = Injector.create(c -> {
            c.bind(SampleA.class);
            c.bind(SampleB.class);
            c.match(only(SampleA.class)).watch(listener);
        });

        var a = i.getInstance(SampleA.class);
        i.getInstance(SampleB.class);

        assertSame(a, listener.instance);
        assertEquals(1, listener.provisioned);
    }

    @Test
    void testOr() {
        var listener = new Listener<>();

        var i = Injector.create(c -> {
            c.bind(SampleA.class);
            c.bind(SampleB.class);
            c.bind(SampleC.class);
            c.match(like(SampleA.class).or(like(SampleB.class))).watch(listener);
        });

        var a = i.getInstance(SampleA.class);
        assertSame(a, listener.instance);

        var b = i.getInstance(SampleB.class);
        assertSame(b, listener.instance);

        i.getInstance(SampleC.class);

        assertEquals(2, listener.provisioned);
    }

    @Test
    void testAttach() {
        var listener = new Listener<SampleB>();
        var i = Injector.create(c -> {
            c.bind(SampleA.class);
            c.bind(SampleB.class);
            c.match(only(SampleA.class)).attach(SampleB.class);
            c.match(only(SampleB.class)).watch(listener);
        });

        i.getInstance(SampleA.class);

        assertEquals(1, listener.provisioned);
    }

    @Test
    void testWatchKey() {
        var listener = new ListenerA();
        var i = Injector.create(c -> {
            c.bind(ListenerA.class).toInstance(listener);
            c.bind(SampleA.class);
            c.match(only(SampleA.class)).watch(ListenerA.class);
        });

        i.getInstance(SampleA.class);

        assertEquals(1, listener.provisioned);
    }

    @Test
    void testWatchKeyAny() {
        var listener = new ListenerAny();
        var i = Injector.create(c -> {
            c.bind(ListenerAny.class).toInstance(listener);
            c.bind(SampleA.class);
            c.match(any()).watch(ListenerAny.class);
        });

        i.getInstance(SampleA.class);

        assertEquals(1, listener.provisioned);
    }

    @Test
    void testEncounterAny() {
        var encounter = new EncounterAny();
        var i = Injector.create(c -> {
            c.bind(SampleA.class);
            c.match(any()).configure(encounter);
        });

        assertSame(encounter, i.getBinding(SampleA.class).getAttribute(EncounterAny.class));
        assertEquals(1, encounter.configured);
    }

    @Test
    void testEncounterKeyAny() {
        var encounter = new EncounterAny();
        var i = Injector.create(c -> {
            c.bind(EncounterAny.class).toInstance(encounter);
            c.bind(SampleA.class);
            c.match(any()).configure(EncounterAny.class);
        });

        assertSame(encounter, i.getBinding(SampleA.class).getAttribute(EncounterAny.class));
        assertEquals(1, encounter.configured);
    }

    @Test
    void testEncounterOnly() {
        var encounter = new EncounterA();
        var i = Injector.create(c -> {
            c.bind(SampleA.class);
            c.match(only(SampleA.class)).configure(encounter);
        });

        assertSame(encounter, i.getBinding(SampleA.class).getAttribute(EncounterA.class));
        assertEquals(1, encounter.configured);
    }

    @Test
    void testEncounterOnlyKey() {
        var encounter = new EncounterA();
        var i = Injector.create(c -> {
            c.bind(EncounterA.class).toInstance(encounter);
            c.bind(SampleA.class);
            c.match(only(SampleA.class)).configure(EncounterA.class);
        });

        assertSame(encounter, i.getBinding(SampleA.class).getAttribute(EncounterA.class));
        assertEquals(1, encounter.configured);
    }

    @Test
    void testAttachmentAny() {
        var i = Injector.create(c -> {
            c.bind(Attachment.class);
            c.bind(SampleA.class);
            c.match(only(SampleA.class)).attach(Attachment.class);
        });

        i.getInstance(SampleA.class);
        assertEquals(1, attached);
    }

    public static class SampleA {

    }

    public static class SampleB {

    }

    public static class SampleC {

    }

    public static class Listener<T> implements ProvisionListener<T> {
        int provisioned = 0;
        T instance = null;

        @Override
        public void provision(T instance) {
            this.instance = instance;
            provisioned += 1;
        }
    }

    public static class ListenerAny extends Listener<Object> {

    }

    public static class ListenerA extends Listener<SampleA> {

    }

    public static class TestEncounter<T> implements Encounter<T> {
        int configured = 0;

        @Override
        public void configure(Binding<T> binding) throws ConfigurationException {
            attr(binding);
            configured += 1;
        }

        void attr(Binding<T> binding) {
            binding.setAttribute(TestEncounter.class, this);
        }
    }

    public static class EncounterAny extends TestEncounter<Object> {
        @Override
        void attr(Binding<Object> binding) {
            binding.setAttribute(EncounterAny.class, this);
        }
    }

    public static class EncounterA extends TestEncounter<SampleA> {
        @Override
        void attr(Binding<SampleA> binding) {
            binding.setAttribute(EncounterA.class, this);
        }
    }

    public static int attached = 0;
    public static class Attachment {
        public Attachment() {
            attached += 1;
        }
    }
}
