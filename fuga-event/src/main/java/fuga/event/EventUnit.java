package fuga.event;

import fuga.common.annotation.AnnotationUtils;
import fuga.event.support.DefaultEventBus;
import fuga.inject.Configuration;
import fuga.inject.Unit;
import fuga.lang.TypeLiteral;
import fuga.logging.LoggingUnit;
import fuga.util.Matcher;
import fuga.util.concurrent.CurrentThreadExecutor;

import java.util.Arrays;
import java.util.concurrent.Executor;

public class EventUnit implements Unit {
    private static final Matcher<TypeLiteral<Object>> MATCHER = t -> Arrays.stream(t.getRawType().getMethods())
            .anyMatch(m -> AnnotationUtils.hasAnnotation(m, Subscribe.class));

    private final DefaultEventBus eventBus;

    public EventUnit() {
        this(CurrentThreadExecutor.INSTANCE);
    }

    public EventUnit(Executor executor) {
        this.eventBus = new DefaultEventBus(executor);
    }

    @Override
    public void setup(Configuration c) {
        c.depends(LoggingUnit.class);

        c.bind(EventBus.class).toInstance(eventBus);

        c.bind(BindingConfigurator.class);
        c.bind(BindingAgent.class);

        c.match(MATCHER).configure(BindingConfigurator.class);
        c.match(MATCHER).attach(BindingAgent.class);
    }
}
