package fuga.inject.support;

import fuga.common.errors.ErrorMessages;

abstract class AbstractWatchingProcessor implements KeyedWatchingProcessor, MatchedWatchingProcessor {

    private final Container container;
    private final ErrorMessages errorMessages;

    AbstractWatchingProcessor(Container container, ErrorMessages errorMessages) {
        this.container = container;
        this.errorMessages = errorMessages;
    }

    void putBinding(AbstractMatchedWatching watching) {
        container.putMatchedWatching(watching);
    }

    <T> void putBinding(AbstractKeyedWatching<T> watching) {
        container.putKeyedWatching(watching);
    }

    ErrorMessages getErrorMessages() {
        return errorMessages;
    }
}
