package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.annotation.AnnotationUtils;
import com.bunjlabs.fuga.inject.ConfigurationException;
import com.bunjlabs.fuga.inject.Inject;
import com.bunjlabs.fuga.inject.Key;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InjectionPoint {
    private final Member member;
    private final List<Dependency<?>> dependencies;
    private final Map<Class<? extends Annotation>, Annotation> annotations;
    private final boolean optional;

    InjectionPoint(Constructor<?> constructor) {
        this(constructor, Dependency.fromMember(constructor, constructor.getParameterAnnotations()),
                Collections.emptyMap(), false);
    }

    public InjectionPoint(Member member, List<Dependency<?>> dependencies, Map<Class<? extends Annotation>, Annotation> annotations, boolean optional) {
        this.member = member;
        this.dependencies = dependencies;
        this.annotations = annotations;
        this.optional = optional;
    }

    static <T> InjectionPoint forConstructor(Constructor<T> constructor) {
        var annotationsMap = new HashMap<Class<? extends Annotation>, Annotation>();
        var annotations = AnnotationUtils.getAllAnnotations(constructor);

        annotations.forEach(a -> annotationsMap.put(a.annotationType(), a));

        return new InjectionPoint(
                constructor, Dependency.fromMember(constructor, constructor.getParameterAnnotations()),
                annotationsMap, false);
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

    boolean containsAnnotation(Class<? extends Annotation> annotationType) {
        return annotations.containsKey(annotationType);
    }

    <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return doGetAnnotation(annotationType);
    }

    @SuppressWarnings("unchecked")
    private <T extends Annotation> T doGetAnnotation(Class<T> annotationType) {
        return (T) annotations.get(annotationType);
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
