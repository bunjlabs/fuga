package com.bunjlabs.fuga.util;

public abstract class MethodUtils {
    public static boolean isParameterTypesEqual(Class<?>[] params1, Class<?>[] params2) {
        if (params1.length != params2.length) return false;

        for (int i = 0; i < params1.length; i++) {
            if (params1[i] != params2[i]) return false;

        }

        return true;
    }
}
