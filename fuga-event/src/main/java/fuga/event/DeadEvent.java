package fuga.event;

import fuga.util.ObjectUtils;

public class DeadEvent {

    private final Object source;
    private final Object event;

    public DeadEvent(Object source, Object event) {
        this.source = source;
        this.event = event;
    }

    public Object getSource() {
        return source;
    }

    public Object getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(DeadEvent.class)
                .add("source", source)
                .add("event", event)
                .toString();
    }
}
