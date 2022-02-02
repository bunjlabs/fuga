package fuga.event;

import fuga.inject.Inject;
import fuga.inject.InjectHint;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BindingAgent {
    private final Logger logger;
    private final EventBus eventBus;

    private final List<Subscription<?>> subscriptions;

    @Inject
    public BindingAgent(Logger logger, EventBus eventBus,
                        @InjectHint(target = InjectHint.Target.ATTRIBUTE, nullable = true) BindingSubscriptions subscriberMethods,
                        @InjectHint(target = InjectHint.Target.SOURCE, nullable = true) Object instance) {
        this.logger = logger;
        this.eventBus = eventBus;

        if (subscriberMethods == null || instance == null) {
            subscriptions = null;
        } else {
            subscriptions = new ArrayList<>(subscriberMethods.size());
            subscribe(subscriberMethods, instance);
        }
    }

    private void subscribe(BindingSubscriptions subscriberMethods, Object instance) {
        subscriberMethods.forEach(s -> {
            var subscription = eventBus.subscribe(s.getEventType(), event -> {
                try {
                    invokeSubscriberMethod(s.getMethod(), instance, event);
                } catch (InvocationTargetException e) {
                    logger.error("Handler exception", e.getCause());
                }
            });

            subscriptions.add(subscription);
        });
    }

    private void invokeSubscriberMethod(Method m, Object instance, Object event) throws InvocationTargetException {
        try {
            m.invoke(instance, event);
        } catch (IllegalAccessException e) {
            throw new Error("Method became inaccessible: " + event, e);
        } catch (IllegalArgumentException e) {
            throw new Error("Method rejected target/argument: " + event, e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
            throw e;
        }
    }
}
