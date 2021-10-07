package fuga.event;

import fuga.common.annotation.AnnotationUtils;
import fuga.inject.Inject;
import fuga.common.Key;
import fuga.inject.ProvisionListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class InjectSubscriberRegistrar implements ProvisionListener {

    private final EventBus eventBus;

    @Inject
    public InjectSubscriberRegistrar(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public <T> void onProvision(Key<T> key, T instance) {
        Arrays.stream(key.getRawType().getMethods())
                .filter(m -> AnnotationUtils.hasAnnotation(m, Subscribe.class))
                .forEach(m -> register(m, instance));
    }

    private <T> void register(Method m, T instance) {
        var parameters = m.getParameters();
        if(parameters.length != 1) {
            throw new RuntimeException("Subscriber method must only have one parameter");
        }

        var eventType = Key.of(parameters[0].getType());

        eventBus.subscribe(eventType, event -> {
            try {
                m.invoke(instance, event);
            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            } catch (RuntimeException e) {

            }
        });
    }
}
