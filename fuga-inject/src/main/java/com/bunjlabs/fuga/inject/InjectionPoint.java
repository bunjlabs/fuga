/*
 * Copyright 2019 Bunjlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bunjlabs.fuga.inject;

import com.bunjlabs.fuga.common.annotation.AnnotationUtils;

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

    public InjectionPoint(Constructor<?> constructor) {
        this(constructor, Dependency.fromMember(constructor, constructor.getParameterAnnotations()),
                Collections.emptyMap(), false);
    }

    public InjectionPoint(Member member, List<Dependency<?>> dependencies, Map<Class<? extends Annotation>, Annotation> annotations, boolean optional) {
        this.member = member;
        this.dependencies = dependencies;
        this.annotations = annotations;
        this.optional = optional;
    }

    public static <T> InjectionPoint forConstructor(Constructor<T> constructor) {
        var annotationsMap = new HashMap<Class<? extends Annotation>, Annotation>();
        var annotations = AnnotationUtils.getAllAnnotations(constructor);

        annotations.forEach(a -> annotationsMap.put(a.annotationType(), a));

        return new InjectionPoint(
                constructor, Dependency.fromMember(constructor, constructor.getParameterAnnotations()),
                annotationsMap, false);
    }

    public static <T> InjectionPoint forConstructorOf(Key<T> key) {
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

    public Member getMember() {
        return member;
    }

    public boolean containsAnnotation(Class<? extends Annotation> annotationType) {
        return annotations.containsKey(annotationType);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
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
