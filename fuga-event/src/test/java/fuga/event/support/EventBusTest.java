package fuga.event.support;

import fuga.event.Subscriber;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;

public class EventBusTest {

    @Test
    void basic() {
        var bus = new DefaultEventBus();
        var listenerA = new EventListener<EventA>();
        var listenerB = new EventListener<EventB>();

        bus.subscribe(EventA.class, listenerA);
        bus.subscribe(EventB.class, listenerB);

        bus.post(new EventA());
        assertEquals(1, listenerA.fired);
        assertEquals(0, listenerB.fired);

        listenerA.reset();
        listenerB.reset();

        bus.post(new EventB());
        assertEquals(0, listenerA.fired);
        assertEquals(1, listenerB.fired);
    }

    @Test
    void unsubscribe() {
        var bus = new DefaultEventBus();
        var listener = new EventListener<EventA>();
        var subscription = bus.subscribe(EventA.class, listener);
        subscription.unsubscribe();

        bus.post(new EventA());
        assertEquals(0, listener.fired);
    }

    @Test
    void executor() {
        var defExecutor = new TestExecutor();
        var subExecutor = new TestExecutor();
        var bus = new DefaultEventBus(defExecutor);

        var listenerA = new EventListener<EventA>();
        var listenerB = new EventListener<EventB>();
        bus.subscribe(EventA.class, listenerA);
        bus.subscribe(EventB.class, listenerB, subExecutor);

        bus.post(new EventA());
        assertEquals(1, defExecutor.executed);
        assertEquals(0, subExecutor.executed);

        defExecutor.reset();
        subExecutor.reset();

        bus.post(new EventB());
        assertEquals(0, defExecutor.executed);
        assertEquals(1, subExecutor.executed);
    }

    public static class EventA {

    }

    public static class EventB {

    }

    public static class TestExecutor implements Executor {
        int executed = 0;

        @Override
        public void execute(Runnable r) {
            executed += 1;
            r.run();
        }

        void reset() {
            executed = 0;
        }
    }

    public static class EventListener<T> implements Subscriber<T> {
        int fired = 0;

        @Override
        public void onEvent(T event) {
            fired += 1;
        }

        void reset() {
            fired = 0;
        }
    }
}
