package com.bunjlabs.fuga.util;

import org.junit.jupiter.api.Test;

import static com.bunjlabs.fuga.util.ReflectionUtils.getParameterTypes;
import static com.bunjlabs.fuga.util.ReflectionUtils.isObjectMethod;
import static org.junit.jupiter.api.Assertions.*;

public class ReflectionUtilsTest {

    @Test
    public void testMethodParametersTypes() throws Exception {
        var simpleConstructor = Sample.class.getConstructor();
        var simpleMethod = Sample.class.getMethod("simpleMethod");
        var constructorParameters = new Class<?>[]{I.class, int.class, Object.class};
        var constructor = Sample.class.getConstructor(constructorParameters);
        var methodParameters = new Class<?>[]{int.class, Object.class, I.class};
        var method = Sample.class.getMethod("method", methodParameters);
        assertTrue(() -> getParameterTypes(simpleMethod).toArray().length == 0);
        assertTrue(() -> getParameterTypes(simpleConstructor).toArray().length == 0);
        assertArrayEquals(constructorParameters, getParameterTypes(constructor).toArray());
        assertArrayEquals(methodParameters, getParameterTypes(method).toArray());
        assertThrows(IllegalArgumentException.class, () -> getParameterTypes(null));
        assertThrows(IllegalArgumentException.class, () -> getParameterTypes(Sample.class.getField("field")));
    }

    @Test
    public void testIsObjectMethod() throws Exception {
        var simpleMethod = Sample.class.getMethod("simpleMethod");
        var toString = Sample.class.getMethod("toString");
        var hashCode = Sample.class.getMethod("hashCode");
        assertFalse(() -> isObjectMethod(simpleMethod));
        assertTrue(() -> isObjectMethod(toString));
        assertTrue(() -> isObjectMethod(hashCode));
    }

    interface I {

    }

    static class Sample {
        public String field;

        public Sample() {

        }

        public Sample(I a, int b, Object c) {

        }

        public void simpleMethod() {

        }

        public void method(int a, Object b, I c) {

        }
    }
}
