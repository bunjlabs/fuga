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

import org.junit.jupiter.api.Test;

import static fuga.util.ReflectionUtils.getParameterTypes;
import static fuga.util.ReflectionUtils.isObjectMethod;
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
