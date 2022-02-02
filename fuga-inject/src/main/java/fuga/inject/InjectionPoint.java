/*
 * Copyright 2019-2021 Bunjlabs
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

package fuga.inject;

import fuga.lang.TypeLiteral;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InjectionPoint {

    private final Member member;
    private final List<Dependency<?>> dependencies;
    private final boolean optional;

    private InjectionPoint(Member member, List<Dependency<?>> dependencies, boolean optional) {
        this.member = member;
        this.dependencies = dependencies;
        this.optional = optional;
    }

    public static <T> InjectionPoint forConstructor(Constructor<T> constructor) {
        return new InjectionPoint(
                constructor, Dependency.fromMember(constructor, TypeLiteral.of(constructor.getDeclaringClass()), constructor.getParameterAnnotations()), false);
    }

    public static <T> InjectionPoint forConstructor(Constructor<T> constructor, TypeLiteral<T> declaredType) {
        return new InjectionPoint(
                constructor, Dependency.fromMember(constructor, declaredType, constructor.getParameterAnnotations()), false);
    }

    public static <T> InjectionPoint forConstructorOf(TypeLiteral<T> type) {
        var injectableConstructors = getInjectableConstructors(type.getRawType());

        if (injectableConstructors.size() > 1) {
            throw new ConfigurationException("Too many injectable constructors in " + type);
        }

        if (!injectableConstructors.isEmpty()) {
            return forConstructor(injectableConstructors.get(0), type);
        }

        // If no marked constructor is found, look for a default one instead
        try {
            return forConstructor(type.getRawType().getConstructor(), type);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException("No marked or default constructor in " + type);
        }
    }

    private static <T> List<Constructor<T>> getInjectableConstructors(Class<T> type) {
        @SuppressWarnings("unchecked")
        var constructors = (Constructor<T>[]) type.getConstructors();

        return Stream.of(constructors)
                .filter(c -> c.isAnnotationPresent(Inject.class))
                .collect(Collectors.toUnmodifiableList());
    }

    public Member getMember() {
        return member;
    }

    public List<Dependency<?>> getDependencies() {
        return dependencies;
    }

    public boolean isOptional() {
        return optional;
    }
}
