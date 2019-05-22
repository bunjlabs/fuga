package com.bunjlabs.fuga.settings.environment;

public class ImmutableEnvironment implements Environment {

    private final String name;

    public ImmutableEnvironment(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
