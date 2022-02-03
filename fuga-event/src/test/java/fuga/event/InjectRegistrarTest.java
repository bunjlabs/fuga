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

        /* TODO: review this behavior for correctness and clarity.
         * Linked keys binding obviously is not expected to generate
         * too much internal factories. Actually it can be easily fixed
         * by removing attachments factory processing for linked key
         * bindings. But this change could break some core attachments
         * usage context.
         *
         * On the other side we've got four times events firings for
         * real reason — every key in configured bindings has inherited
         * event listener method. When processing bindings by
         * Watch API we just select classes that has one or more annotated
         * method. Should we look only at declared methods? No, because
         * in some cases we want to use this "inheritance" behavior
         * (for example, this could be useful in `fuga-app` package).
         * Should we interpret linked key binding as exceptional case?
         * I can not say for sure because there no known identical problems
         * with scoping and provision listeners.
         *
         * Maybe it's not a problem at all but this issue
         * makes think about attachments concept in another way.
         *
         * Decision required — what to expect, four or one?
         */
        assertEquals(4, sample.events.size());
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
