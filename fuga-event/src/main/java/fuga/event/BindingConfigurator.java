package fuga.event;

import fuga.common.Key;
import fuga.common.annotation.AnnotationUtils;
import fuga.inject.Binding;
import fuga.inject.ConfigurationException;
import fuga.inject.Encounter;

import java.util.ArrayList;
import java.util.Arrays;

public class BindingConfigurator implements Encounter<Object> {

    @Override
    public void configure(Binding<Object> binding) throws ConfigurationException {
        var type = binding.getKey().getRawType();

        var subscriberMethods = new ArrayList<SubscriberMethod>();
        Arrays.stream(type.getMethods()).forEach(method -> {
            var subscribe = AnnotationUtils.findAnnotation(method, Subscribe.class);
            if (subscribe == null) {
                return;
            }

            var parameters = method.getParameters();
            if (parameters.length != 1) {
                throw new ConfigurationException("Subscriber method must only have one parameter");
            }

            var eventType = Key.of(parameters[0].getType());

            subscriberMethods.add(new SubscriberMethod(method, eventType, subscribe));
        });

        if (!subscriberMethods.isEmpty()) {
            binding.setAttribute(BindingSubscriptions.class, new BindingSubscriptions(subscriberMethods));
        }
    }
}
