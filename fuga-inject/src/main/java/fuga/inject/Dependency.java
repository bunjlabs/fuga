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

import fuga.common.Key;
import fuga.common.annotation.AnnotationUtils;
import fuga.inject.InjectHint.Target;
import fuga.lang.TypeLiteral;
import fuga.util.ObjectUtils;
import fuga.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static fuga.util.Assert.notNull;

public class Dependency<T> {

    enum Modifier {
        ALL, NULLABLE
    }

    private final Key<T> key;
    private final EnumSet<Modifier> modifiers;
    private final Target target;

    private final Key<?> realKey;
    private final int hashCode;
    private String toString;

    private Dependency(Key<T> key, EnumSet<Modifier> modifiers, Target target, Key<?> realKey) {
        this.key = notNull(key);
        this.modifiers = modifiers;
        this.target = target;
        this.realKey = realKey;
        this.hashCode = computeHashCode();
    }

    public static <T> Dependency<T> of(Key<T> key) {
        return new Dependency<>(key, EnumSet.noneOf(Modifier.class), Target.BINDING, key);
    }

    public static <T> Dependency<T> of(Class<T> clazz) {
        return of(Key.of(clazz));
    }

    public static <T> Dependency<Collection<T>> ofAll(Key<T> key) {
        @SuppressWarnings("unchecked")
        var collectionKey = (Key<Collection<T>>) Key.<Collection<T>>of(Collection.class);
        return new Dependency<>(collectionKey, EnumSet.of(Modifier.ALL), Target.BINDING, key);
    }

    public static <T> Dependency<Collection<T>> ofAll(Class<T> clazz) {
        return ofAll(Key.of(clazz));
    }

    public static List<Dependency<?>> fromMember(Member member, TypeLiteral<?> declaredType, Annotation[][] parametersAnnotations) {
        var dependencies = new ArrayList<Dependency<?>>();

        int index = 0;
        for (TypeLiteral<?> parameterType : declaredType.getParameterTypes(member)) {
            var parameterAnnotations = parametersAnnotations[index];
            var parameterKey = Key.of(parameterType);

            var hint = AnnotationUtils.findAnnotation(parameterAnnotations, InjectHint.class);
            var target = hint == null ? Target.BINDING : hint.target();

            var modifiers = EnumSet.noneOf(Modifier.class);
            if (ReflectionUtils.allowsNull(parameterAnnotations) || (hint != null && hint.nullable())) {
                modifiers.add(Modifier.NULLABLE);
            }

            if (AnnotationUtils.hasAnnotation(parameterAnnotations, InjectAll.class) || (hint != null && hint.all())) {
                modifiers.add(Modifier.ALL);
            }

            var realKey = parameterKey;
            if (modifiers.contains(Modifier.ALL)) {
                if (!parameterKey.getRawType().isAssignableFrom(Set.class)) {
                    throw new ConfigurationException("Not a Set" + parameterKey);
                }

                var realType = ((ParameterizedType) parameterType.getType()).getActualTypeArguments();
                realKey = Key.of(realType[0]);
            }

            dependencies.add(new Dependency<>(parameterKey, modifiers, target, realKey));

            index++;
        }

        return List.copyOf(dependencies);
    }

    private int computeHashCode() {
        return Objects.hash(key, modifiers);
    }

    public Key<T> getKey() {
        return key;
    }

    public boolean isNullable() {
        return modifiers.contains(Modifier.NULLABLE);
    }

    public boolean isRequestedAll() {
        return modifiers.contains(Modifier.ALL);
    }

    public boolean isTargetAttr() {
        return target == Target.ATTRIBUTE;
    }

    public boolean isTargetSource() {
        return target == Target.SOURCE;
    }

    public Key<?> getRealKey() {
        return realKey;
    }

    public TypeLiteral<T> getType() {
        return key.getType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency<?> dep = (Dependency<?>) o;
        return key.equals(dep.key)
                && modifiers.equals(dep.modifiers);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        String s = toString;
        if (toString == null) {
            var j = ObjectUtils.toStringJoiner(this)
                    .add(key.getType());
            if (!modifiers.isEmpty()) {
                j.add(modifiers);
            }
            toString = s = j.toString();
        }
        return s;
    }
}
