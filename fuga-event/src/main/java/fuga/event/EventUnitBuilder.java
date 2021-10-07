package fuga.event;

import fuga.common.annotation.AnnotationUtils;
import fuga.event.support.DefaultEventBus;
import fuga.inject.Singleton;
import fuga.inject.Unit;
import fuga.inject.UnitBuilder;

import java.util.Arrays;

public class EventUnitBuilder implements UnitBuilder {

    @Override
    public Unit build() {
        return c -> {
            c.watch(t -> Arrays.stream(t.getRawType().getMethods()).anyMatch(m ->
                        AnnotationUtils.hasAnnotation(m, Subscribe.class))).with(InjectSubscriberRegistrar.class);

            c.bind(DefaultEventBus.class).in(Singleton.class);
            c.bind(EventBus.class).to(DefaultEventBus.class);

            c.bind(InjectSubscriberRegistrar.class).in(Singleton.class);
        };
    }
}
