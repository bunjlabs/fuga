package fuga.event;

import fuga.inject.UnitBuilder;
import fuga.util.concurrent.CurrentThreadExecutor;

import java.util.concurrent.Executor;

public class EventUnitBuilder implements UnitBuilder<EventUnit> {

    private Executor executor = CurrentThreadExecutor.INSTANCE;

    public EventUnitBuilder withExecutor(Executor executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public EventUnit build() {
        return new EventUnit(executor);
    }
}
