package fuga.logging;

import fuga.inject.Configuration;
import fuga.inject.Unit;
import fuga.logging.support.DefaultLoggerComposer;
import org.slf4j.Logger;

public class LoggingUnit implements Unit {
    @Override
    public void setup(Configuration c) {
        var composer = new DefaultLoggerComposer();

        c.bind(LoggerComposer.class).toInstance(composer);
        c.bind(Logger.class).toComposer(composer);
    }
}
