package com.bunjlabs.fuga.util;

import java.io.Serializable;
import java.lang.reflect.*;

public class FullType<T> {
    public static final FullType<EmptyType> EMPTY = new FullType<>(EmptyType.TYPE);
    public static final FullType[] EMPTY_ARRAY = new FullType[0];

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

    public static <T> FullType<T> of(Class<T> type) {
        return new FullType<>(type);
    }

    private static Class<?> resolveType(Type type) {
        if (type == EmptyType.TYPE) return EmptyType.class;

        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            var parameterizedType = (ParameterizedType) type;
            var rawType = parameterizedType.getRawType();
            Assert.isTrue(rawType instanceof Class);
            return (Class<?>) rawType;
        } else if (type instanceof GenericArrayType) {
            var componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(resolveType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable || type instanceof WildcardType) {
            return Object.class;
        } else {
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
        }
    }

    public FullType as(Class<?> type) {
        if (this == EMPTY) {
            return EMPTY;
        }
        if (rawType == null || rawType == type) {
            return this;
        }

        for (FullType<?> interfaceType : getInterfaces()) {
            FullType interfaceAsType = interfaceType.as(type);
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

    public FullType getSuperType() {
        if (rawType == null || rawType.getGenericSuperclass() == null) {
            return EMPTY;
        }
        if (superType == null) {
            superType = of(rawType.getGenericSuperclass());
        }
        return superType;
    }

    public FullType[] getInterfaces() {
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
        FullType[] generics = getGenerics();
        if (indexes == null || indexes.length == 0) {
            return (generics.length == 0 ? EMPTY : generics[0]);
        }
        FullType generic = this;
        for (int index : indexes) {
            generics = generic.getGenerics();
            if (index < 0 || index >= generics.length) {
                return EMPTY;
            }
            generic = generics[index];
        }
        return generic;
    }

    public FullType[] getGenerics() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof FullType<?> && type.equals(((FullType) o).type);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        String s = toString;
        if (s == null) {
            s = type instanceof Class ? ((Class) type).getName() : type.toString();
            toString = s;
        }
        return s;
    }

    static class EmptyType implements Type, Serializable {
        static final Type TYPE = new EmptyType();
    }
}
