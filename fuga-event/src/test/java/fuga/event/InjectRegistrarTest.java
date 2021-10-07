package fuga.event;

import fuga.inject.Injector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InjectRegistrarTest {

    @Test
    void basic() {
        var injector = Injector.create(new EventUnitBuilder().build(), c -> {
            c.bind(Sample.class);
        });

        var sample = injector.getInstance(Sample.class);
        injector.getInstance(EventBus.class).fire(new EventA());

        assertTrue(sample.eventFired);
    }

    public static class EventA {

    }

    public static class EventB {

    }

    public static class Sample {
        public boolean eventFired = false;

        @Subscribe
        public void onEvent(EventA e) {
            eventFired = true;
        }
    }
}
