package fuga.event;


import java.util.Iterator;
import java.util.List;

public class BindingSubscriptions implements Iterable<SubscriberMethod> {
    private final List<SubscriberMethod> subscriberMethods;

    public BindingSubscriptions(List<SubscriberMethod> subscriberMethods) {
        this.subscriberMethods = subscriberMethods;
    }


    @Override
    public Iterator<SubscriberMethod> iterator() {
        return subscriberMethods.iterator();
    }

    public int size() {
        return subscriberMethods.size();
    }
}
