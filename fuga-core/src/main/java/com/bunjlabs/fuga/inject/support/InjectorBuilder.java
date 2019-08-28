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

        for (var unit : installedUnits) {
            configureUnit(unit, container);
        }

        ContainerProcessor processor = new DefaultContainerProcessor();
        processor.process(container);

        return new DefaultInjector(container);
    }

    private void configureUnit(Unit unit, Container container) {
        var configuration = new DefaultConfiguration();

        try {
            unit.configure(configuration);
        } catch (RuntimeException e) {
            throw new ConfigurationException("Unable to configure unit " + unit, e);
        }

        for (var binding : configuration.getBindings()) {
            container.putBinding(binding);
        }

        for (var innerUnit : configuration.getInstalledUnits()) {
            configureUnit(innerUnit, container);
        }
    }
}
