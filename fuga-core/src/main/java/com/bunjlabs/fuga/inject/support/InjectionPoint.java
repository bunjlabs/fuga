package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.ConfigurationException;
import com.bunjlabs.fuga.inject.Inject;
import com.bunjlabs.fuga.inject.Key;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InjectionPoint {
    private final Member member;
    private final List<Dependency<?>> dependencies;
    private final boolean optional;

    InjectionPoint(Constructor<?> constructor) {
        this.member = constructor;
        this.optional = false;
        this.dependencies = Dependency.fromMember(member, constructor.getParameterAnnotations());
    }

    static <T> InjectionPoint forConstructor(Constructor<T> constructor) {
        return new InjectionPoint(constructor);
    }

    static <T> InjectionPoint forConstructorOf(Key<T> key) {
        var type = key.getType();
        var injectableConstructors = getInjectableConstructors(type);

        if (injectableConstructors.size() > 1) {
            throw new ConfigurationException("Too many injectable constructors in " + key.getType());
        }

        if (!injectableConstructors.isEmpty()) {
            return forConstructor(injectableConstructors.get(0));
        }

        // If no marked constructor is found, look for a default one instead
        try {
            return forConstructor(type.getDeclaredConstructor());
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException("No marked or default constructor in " + key.getType());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> List<Constructor<T>> getInjectableConstructors(Class<T> type) {
        Constructor<T>[] constructors = (Constructor<T>[]) type.getConstructors();

        return Stream.of(constructors)
                .filter(c -> c.isAnnotationPresent(Inject.class))
                .collect(Collectors.toUnmodifiableList());
    }

    Member getMember() {
        return member;
    }

    public List<Dependency<?>> getDependencies() {
        return dependencies;
    }

    public boolean isOptional() {
        return optional;
    }

    public Class<?> getDeclaringClass() {
        return member.getDeclaringClass();
    }

}
