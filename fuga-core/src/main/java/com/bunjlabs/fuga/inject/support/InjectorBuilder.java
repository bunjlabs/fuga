package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.ConfigurationException;
import com.bunjlabs.fuga.inject.Injector;
import com.bunjlabs.fuga.inject.Unit;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InjectorBuilder {

    private final List<Unit> installedUnits = new LinkedList<>();

    public InjectorBuilder withUnits(Unit... units) {
        this.installedUnits.addAll(Arrays.asList(units));
        return this;
    }

    public InjectorBuilder withUnits(Iterable<Unit> units) {
        units.forEach(installedUnits::add);
        return this;
    }

    public Injector build() {

        Container container = new InheritedContainer(Container.EMPTY);
        BindingProcessor bindingProcessor = new DefaultBindingProcessor(container);

        for (var unit : installedUnits) {
            setupUnit(unit, bindingProcessor);
        }

        return new DefaultInjector(container);
    }

    private void setupUnit(Unit unit, BindingProcessor bindingProcessor) {
        var configuration = new DefaultConfiguration(bindingProcessor);

        try {
            unit.setup(configuration);
        } catch (RuntimeException e) {
            throw new ConfigurationException("Unable to setup unit " + unit, e);
        }

        for (var innerUnit : configuration.getInstalledUnits()) {
            setupUnit(innerUnit, bindingProcessor);
        }
    }
}
