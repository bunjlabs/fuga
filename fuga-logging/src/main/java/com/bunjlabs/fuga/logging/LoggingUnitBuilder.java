package com.bunjlabs.fuga.logging;

import com.bunjlabs.fuga.inject.Unit;
import com.bunjlabs.fuga.inject.UnitBuilder;
import com.bunjlabs.fuga.logging.support.DefaultLoggerComposer;
import org.slf4j.Logger;

public class LoggingUnitBuilder implements UnitBuilder {
    public LoggingUnitBuilder() {
    }

    @Override
    public Unit build() {
        return c -> {
            var composer = new DefaultLoggerComposer();

            c.bind(LoggerComposer.class).toInstance(composer);
            c.bind(Logger.class).toComposer(composer);
        };
    }
}
