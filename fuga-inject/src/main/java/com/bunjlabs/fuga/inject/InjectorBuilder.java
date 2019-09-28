package com.bunjlabs.fuga.inject;

import com.bunjlabs.fuga.inject.support.InternalInjectorBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InjectorBuilder {

    private final List<Unit> units = new LinkedList<>();

    public InjectorBuilder withUnits(Unit... units) {
        this.units.addAll(Arrays.asList(units));
        return this;
    }

    public InjectorBuilder withUnits(Iterable<Unit> units) {
        units.forEach(this.units::add);
        return this;
    }

    public Injector build() {
        return new InternalInjectorBuilder().withUnits(units).build();
    }
}
