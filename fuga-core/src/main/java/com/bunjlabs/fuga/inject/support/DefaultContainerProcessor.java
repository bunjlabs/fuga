package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.annotation.AnnotationUtils;
import com.bunjlabs.fuga.inject.Binding;
import com.bunjlabs.fuga.inject.FabricatedBy;
import com.bunjlabs.fuga.inject.Key;

import java.util.HashMap;

public class DefaultContainerProcessor implements ContainerProcessor {

    @Override
    public void process(Container container) {
        var explicitBindings = new HashMap<>(container.getExplicitBindingsLocal());

        explicitBindings.values().forEach(b -> this.processBinding(container, b));
    }

    private void processBinding(Container container, Binding<?> binding) {
        if (binding instanceof LinkBinding) {
            processLinkBinding(container, (LinkBinding<?>) binding);
        } else if (binding instanceof UntargettedBinding) {
            processUntargettedBinding(container, (UntargettedBinding<?>) binding);
        }
    }


    private void processLinkBinding(Container container, LinkBinding<?> binding) {
        var linkBinding = container.getExplicitBinding(binding.getLinkKey());

        if (linkBinding != null && !linkBinding.equals(binding)) {
            // recursively check this binding chain
            processBinding(container, linkBinding);
        } else {
            // we suppose that this implicit binding is targeting to constructable prototype or factory
            // try to generate corresponding explicit binding
            container.putBinding(createBindingFor(binding.getLinkKey()));
        }
    }

    private void processUntargettedBinding(Container container, UntargettedBinding<?> binding) {
        container.putBinding(createBindingFor(binding.getKey()));
    }

    private <T> Binding<T> createBindingFor(Key<T> key) {
        var fabricatedBy = AnnotationUtils.findAnnotation(key.getType(), FabricatedBy.class);
        if (fabricatedBy != null) {
            var factoryClass = fabricatedBy.value();

            return new FactoryBinding<>();
        }

        return new ConstructorBinding<>(key, InjectionPoint.forConstructorOf(key));
    }
}
