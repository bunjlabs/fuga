package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.ConfigurationException;
import com.bunjlabs.fuga.inject.Injector;
import com.bunjlabs.fuga.inject.Unit;

import java.util.LinkedList;
import java.util.List;

public class InternalInjectorBuilder {

    private final List<Unit> units = new LinkedList<>();
    private Container parent = Container.EMPTY;

    public InternalInjectorBuilder withUnits(Iterable<Unit> units) {
        units.forEach(this.units::add);
        return this;
    }

    public InternalInjectorBuilder withParent(InjectorImpl injector) {
        this.parent = injector.getContainer();
        return this;
    }

    public Injector build() {
        Container container = new InheritedContainer(parent);
        BindingProcessor bindingProcessor = new DefaultBindingProcessor(container);

        for (var unit : units) {
            setupUnit(unit, bindingProcessor);
        }

        return new InjectorImpl(container);
    }

    private void setupUnit(Unit unit, BindingProcessor bindingProcessor) {
        var configuration = new DefaultConfiguration();

        try {
            unit.setup(configuration);
        } catch (RuntimeException e) {
            throw new ConfigurationException("Unable to setup unit " + unit, e);
        }

        for (var innerUnit : configuration.getInstalledUnits()) {
            setupUnit(innerUnit, bindingProcessor);
        }

        for (AbstractBinding<?> binding : configuration.getBindings()) {
            bindingProcessor.process(binding);
        }
    }
}
