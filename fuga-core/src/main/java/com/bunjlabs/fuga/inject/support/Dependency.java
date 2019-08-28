package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Key;
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

    static <T> Dependency<T> of(Key<T> key) {
        return new Dependency<>(key, -1, true);
    }

    static List<Dependency<?>> fromMember(Member member, Annotation[][] parametersAnnotations) {
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
