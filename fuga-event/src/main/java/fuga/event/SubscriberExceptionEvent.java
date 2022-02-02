package fuga.event;

public class SubscriberExceptionEvent {

    private final SubscriberException exception;
    private final EventBus eventBus;
    private final Subscriber<?> subscriber;

    public SubscriberExceptionEvent(SubscriberException exception, EventBus eventBus, Subscriber<?> subscriber) {
        this.exception = exception;
        this.eventBus = eventBus;
        this.subscriber = subscriber;
    }

    public SubscriberException getException() {
        return exception;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Subscriber<?> getSubscriber() {
        return subscriber;
    }
}
