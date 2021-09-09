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

import fuga.common.annotation.AnnotationUtils;
import fuga.util.FullType;
import fuga.util.ObjectUtils;
import fuga.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static fuga.util.Assert.notNull;

public class Dependency<T> {

    private final Key<T> key;
    private final boolean nullable;
    private final boolean requestedAll;
    private final Key<?> realKey;
    private final int hashCode;
    private String toString;

    private Dependency(Key<T> key, boolean nullable, boolean requestedAll) {
        this(key, nullable, requestedAll, key);
    }

    private Dependency(Key<T> key, boolean nullable, boolean requestedAll, Key<?> realKey) {
        this.key = notNull(key);
        this.nullable = nullable;
        this.requestedAll = requestedAll;
        this.realKey = realKey;
        this.hashCode = computeHashCode();
    }

    public static <T> Dependency<T> of(Key<T> key) {
        return new Dependency<>(key, true, false);
    }

    public static <T> Dependency<T> of(Class<T> clazz) {
        return new Dependency<>(Key.of(clazz), true, false);
    }

    public static List<Dependency<?>> fromMember(Member member, FullType<?> declaredType, Annotation[][] parametersAnnotations) {
        var dependencies = new ArrayList<Dependency<?>>();

        int index = 0;
        for (FullType<?> parameterType : declaredType.getParameterTypes(member)) {
            var parameterAnnotations = parametersAnnotations[index];

            var parameterKey = Key.of(parameterType);
            var nullable = ReflectionUtils.allowsNull(parameterAnnotations);
            var requestedAll = AnnotationUtils.hasAnnotation(parameterAnnotations, InjectAll.class);

            if (requestedAll) {
                if (!parameterKey.getRawType().isAssignableFrom(Set.class)) {
                    throw new ConfigurationException("Not a Set" + parameterKey);
                }

                var realType = ((ParameterizedType) parameterType.getType()).getActualTypeArguments();
                var realKey = Key.of(realType[0]);
                dependencies.add(new Dependency<>(parameterKey, nullable, true, realKey));
            } else {
                dependencies.add(new Dependency<>(parameterKey, nullable, false));
            }

            index++;
        }

        return List.copyOf(dependencies);
    }

    private int computeHashCode() {
        return Objects.hash(key, nullable);
    }

    public Key<T> getKey() {
        return key;
    }

    public boolean isNullable() {
        return nullable;
    }

    public boolean isRequestedAll() {
        return requestedAll;
    }

    public Key<?> getRealKey() {
        return realKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency<?> dep = (Dependency<?>) o;
        return key.equals(dep.key)
                && requestedAll == dep.requestedAll
                && nullable == dep.nullable;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        String s = toString;
        if (s == null) {
            s = ObjectUtils.toStringJoiner(this)
                    .add("key", key)
                    .add("requestedAll", requestedAll)
                    .add("nullable", nullable)
                    .toString();
            toString = s;
        }
        return s;
    }
}
