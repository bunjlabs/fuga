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

import com.bunjlabs.fuga.util.ObjectUtils;
import com.bunjlabs.fuga.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.bunjlabs.fuga.util.Assert.notNull;

public class Dependency<T> {

    private final Key<T> key;
    private final int parameterIndex;
    private final boolean nullable;
    private final int hashCode;
    private String toString;

    private Dependency(Key<T> key, int parameterIndex, boolean nullable) {
        this.key = notNull(key);
        this.parameterIndex = parameterIndex;
        this.nullable = nullable;
        this.hashCode = computeHashCode();
    }

    public static <T> Dependency<T> of(Key<T> key) {
        return new Dependency<>(key, -1, true);
    }

    public static <T> Dependency<T> of(Class<T> clazz) {
        return new Dependency<>(Key.of(clazz), -1, true);
    }

    public static List<Dependency<?>> fromMember(Member member, Annotation[][] parametersAnnotations) {
        List<Dependency<?>> dependencies = new ArrayList<>();

        int index = 0;
        for (Class<?> parameterType : ReflectionUtils.getParameterTypes(member)) {
            Annotation[] parameterAnnotations = parametersAnnotations[index];
            Key<?> parameterKey = Key.of(parameterType);
            dependencies.add(new Dependency<>(parameterKey, index, ReflectionUtils.allowsNull(parameterAnnotations)));
            index++;
        }

        return List.copyOf(dependencies);
    }

    private int computeHashCode() {
        return Objects.hash(key, parameterIndex, nullable);
    }

    public Key<T> getKey() {
        return key;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public boolean isNullable() {
        return nullable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency<?> dep = (Dependency<?>) o;
        return key.equals(dep.key)
                && parameterIndex == dep.parameterIndex
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
                    .add("parameterIndex", parameterIndex)
                    .add("nullable", nullable)
                    .toString();
            toString = s;
        }
        return s;
    }
}
