package com.bunjlabs.fuga.environment;

public interface Environment {

    Environment DEFAULT = () -> "";

    String getName();


}
