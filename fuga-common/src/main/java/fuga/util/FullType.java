/*
 * Copyright 2019-2020 Bunjlabs
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

package fuga.util;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.List;

public class FullType<T> {
    public static final FullType<EmptyType> EMPTY = new FullType<>(EmptyType.TYPE);
    public static final FullType<?>[] EMPTY_ARRAY = new FullType[0];

    private final Type type;
    private final Class<T> rawType;
    private final int hashCode;

    private volatile FullType<?> superType;
    private volatile FullType<?>[] interfaces;
    private volatile FullType<?>[] generics;

    private String toString;

    @SuppressWarnings("unchecked")
    private FullType(Type type) {
        this.type = Assert.notNull(type);
        this.rawType = (Class<T>) resolveType(type);
        this.hashCode = type.hashCode();
    }

    public static FullType<?> of(Type type) {
        return new FullType<>(type);
    }

    private static List<FullType<?>> of(Type... types) {
        var result = new FullType<?>[types.length];
        for (int t = 0; t < types.length; t++) {
            result[t] = of(types[t]);
        }
        return List.of(result);
    }

    public static <T> FullType<T> of(Class<T> type) {
        return new FullType<>(type);
    }

    public static List<FullType<?>> of(Class<?>... types) {
        var result = new FullType<?>[types.length];
        for (int t = 0; t < types.length; t++) {
            result[t] = of(types[t]);
        }
        return List.of(result);
    }

    private static Class<?> resolveType(Type type) {
        if (type == EmptyType.TYPE) return EmptyType.class;

        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            var parameterizedType = (ParameterizedType) type;
            var rawType = parameterizedType.getRawType();
            Assert.isTrue(rawType instanceof Class);
            return (Class<?>) rawType;
        } else if (type instanceof GenericArrayType) {
            var componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(resolveType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable) {
            var variable = (TypeVariable<?>) type;
            return resolveType(resolveBounds(variable.getBounds()));
        } else if (type instanceof WildcardType) {
            Type resolved = resolveBounds(((WildcardType) type).getUpperBounds());
            if (resolved == null) {
                resolved = resolveBounds(((WildcardType) type).getLowerBounds());
            }
            return resolveType(resolved);
        } else {
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
        }
    }


    private static Type resolveBounds(Type[] bounds) {
        if (bounds.length == 0) {
            return null;
        }
        return bounds[0];
    }

    public <V> FullType<?> as(Class<V> type) {
        if (this == EMPTY) {
            return EMPTY;
        }
        if (rawType == null || rawType == type) {
            return this;
        }

        for (FullType<?> interfaceType : getInterfaces()) {
            var interfaceAsType = interfaceType.as(type);
            if (interfaceAsType != EMPTY) {
                return interfaceAsType;
            }
        }
        return getSuperType().as(type);
    }

    public Type getType() {
        return type;
    }

    public Class<T> getRawType() {
        return rawType;
    }

    public FullType<?> getSuperType() {
        if (rawType == null || rawType.getGenericSuperclass() == null) {
            return EMPTY;
        }
        if (superType == null) {
            superType = of(rawType.getGenericSuperclass());
        }
        return superType;
    }

    public FullType<?>[] getInterfaces() {
        if (rawType == null) {
            return EMPTY_ARRAY;
        }

        var interfaces = this.interfaces;
        if (interfaces == null) {
            var genericInterfaces = rawType.getGenericInterfaces();
            interfaces = new FullType[genericInterfaces.length];
            for (int i = 0; i < interfaces.length; i++) {
                interfaces[i] = of(genericInterfaces[i]);
            }
            this.interfaces = interfaces;
        }

        return interfaces;
    }

    public FullType<?> getGeneric(int... indexes) {
        var generics = getGenerics();
        if (indexes == null || indexes.length == 0) {
            return (generics.length == 0 ? EMPTY : generics[0]);
        }
        FullType<?> generic = this;
        for (int index : indexes) {
            generics = generic.getGenerics();
            if (index < 0 || index >= generics.length) {
                return EMPTY;
            }
            generic = generics[index];
        }
        return generic;
    }

    public FullType<?>[] getGenerics() {
        if (this == EMPTY) {
            return EMPTY_ARRAY;
        }

        var generics = this.generics;
        if (generics == null) {
            if (type instanceof Class) {
                var typeParameters = ((Class<?>) type).getTypeParameters();
                generics = new FullType[typeParameters.length];
                for (int i = 0; i < generics.length; i++) {
                    generics[i] = of(typeParameters[i]);
                }
            } else if (this.type instanceof ParameterizedType) {
                var actualTypeArguments = ((ParameterizedType) this.type).getActualTypeArguments();
                generics = new FullType[actualTypeArguments.length];
                for (int i = 0; i < generics.length; i++) {
                    generics[i] = of(actualTypeArguments[i]);
                }
            } else {
                generics = EMPTY_ARRAY;
            }
            this.generics = generics;
        }

        return generics;
    }

    public List<FullType<?>> getParameterTypes(Member methodOrConstructor) {
        Type[] genericParameterTypes;

        if (methodOrConstructor instanceof Method) {
            var method = (Method) methodOrConstructor;
            Assert.isTrue(method.getDeclaringClass().isAssignableFrom(rawType), String.format("%s is not defined by a supertype of %s", method, type));
            genericParameterTypes = method.getGenericParameterTypes();
        } else if (methodOrConstructor instanceof Constructor) {
            var constructor = (Constructor<?>) methodOrConstructor;
            Assert.isTrue(constructor.getDeclaringClass().isAssignableFrom(rawType), String.format("%s is not construct a supertype of %s", constructor, type));
            genericParameterTypes = constructor.getGenericParameterTypes();
        } else {
            throw new IllegalArgumentException("Not a method or a constructor: " + methodOrConstructor);
        }

        return of(genericParameterTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof FullType<?> && type.equals(((FullType<?>) o).type);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        String s = toString;
        if (s == null) {
            s = type instanceof Class ? ((Class<?>) type).getName() : type.toString();
            toString = s;
        }
        return s;
    }

    static class EmptyType implements Type, Serializable {
        static final Type TYPE = new EmptyType();
    }
}
