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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public abstract class ReflectionUtils {

    private static final List<Method> OBJECT_METHODS = Arrays.asList(Object.class.getMethods());


    public static List<Class<?>> getParameterTypes(Member methodOrConstructor) {
        Class<?>[] parameterTypes;

        if (methodOrConstructor instanceof Constructor) {
            Constructor<?> constructor = (Constructor<?>) methodOrConstructor;
            parameterTypes = constructor.getParameterTypes();
        } else if (methodOrConstructor instanceof Method) {
            Method method = (Method) methodOrConstructor;
            parameterTypes = method.getParameterTypes();
        } else {
            throw new IllegalArgumentException("Not a method or a constructor: " + methodOrConstructor);
        }

        return List.of(parameterTypes);
    }

    public static boolean allowsNull(Annotation[] annotations) {
        for (var a : annotations) {
            var type = a.annotationType();
            if ("Nullable".equals(type.getSimpleName())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isObjectMethod(Method m) {
        return OBJECT_METHODS.contains(m);
    }
}
