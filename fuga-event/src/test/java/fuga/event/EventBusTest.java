package fuga.event;

import fuga.event.support.DefaultEventBus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventBusTest {

    @Test
    void basic() {
        var bus = new DefaultEventBus();
        var listenerA = new EventListener<EventA>();
        var listenerB = new EventListener<EventB>();

        bus.subscribe(EventA.class, listenerA);
        bus.subscribe(EventB.class, listenerB);

        bus.fire(new EventA());
        assertTrue(listenerA.fired);
        assertFalse(listenerB.fired);

        listenerA.reset();
        listenerB.reset();

        bus.fire(new EventB());
        assertFalse(listenerA.fired);
        assertTrue(listenerB.fired);
    }

    public static class EventA {

    }

    public static class EventB {

    }

    public static class EventListener<T> implements Subscriber<T>{
        boolean fired = false;

        @Override
        public void onEvent(T event) {
            fired = true;
        }

        void reset() {
            fired = false;
        }
    }
}
