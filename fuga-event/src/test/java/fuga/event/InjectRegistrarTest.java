package fuga.event;

import fuga.inject.Injector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class InjectRegistrarTest {

    @Test
    void testBasics() {
        var injector = Injector.create(new EventUnitBuilder().build(), c -> {
            c.bind(Sample.class);
        });

        var sample = injector.getInstance(Sample.class);
        var eventA = new EventA();
        injector.getInstance(EventBus.class).post(eventA);
        injector.getInstance(EventBus.class).post(new EventB());

        assertEquals(1, sample.events.size());
        assertSame(eventA, sample.events.get(0));
    }

    @Test
    void testProvisionChain() {
        var injector = Injector.create(new EventUnitBuilder().build(), c -> {
            c.bind(Sample.class).to(OuterA.class);
            c.bind(OuterA.class).to(OuterB.class);
            c.bind(OuterB.class).to(OuterC.class);
            c.bind(OuterC.class).toProvider(OuterC::new);
        });

        var sample = injector.getInstance(Sample.class);
        var eventA = new EventA();
        injector.getInstance(EventBus.class).post(eventA);
        injector.getInstance(EventBus.class).post(new EventB());

        assertEquals(1, sample.events.size());
        assertSame(eventA, sample.events.get(0));
    }

    public static class EventA {

    }

    public static class EventB {

    }

    public static class Sample {
        public List<EventA> events = new ArrayList<>();

        @Subscribe
        public void onEvent(EventA e) {
            events.add(e);
        }
    }


    public static class OuterA extends Sample {

    }

    public static class OuterB extends OuterA {

    }

    public static class OuterC extends OuterB {

    }
}
