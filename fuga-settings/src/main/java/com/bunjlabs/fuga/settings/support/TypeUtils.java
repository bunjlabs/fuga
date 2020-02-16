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

package com.bunjlabs.fuga.settings.support;

import com.bunjlabs.fuga.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class TypeUtils {

    private static final Map<Class<?>, Function<String, ?>> primitiveParsers = new HashMap<>();

    static {
        primitiveParsers.put(String.class, s -> s);
        primitiveParsers.put(byte.class, Byte::parseByte);
        primitiveParsers.put(short.class, Short::parseShort);
        primitiveParsers.put(int.class, Integer::parseInt);
        primitiveParsers.put(long.class, Long::parseLong);
        primitiveParsers.put(float.class, Float::parseFloat);
        primitiveParsers.put(double.class, Double::parseDouble);
        primitiveParsers.put(boolean.class, Boolean::parseBoolean);
        primitiveParsers.put(char.class, s -> s.charAt(0));
    }

    public static Object convertStringToPrimitive(String input, Class<?> type) {
        Assert.notNull(input);
        Assert.notNull(type);

        var converter = primitiveParsers.get(type);

        return converter.apply(input);
    }
}
