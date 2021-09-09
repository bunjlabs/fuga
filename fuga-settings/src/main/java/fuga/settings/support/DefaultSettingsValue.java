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

package fuga.settings.support;

import fuga.settings.SettingsValue;

public class DefaultSettingsValue implements SettingsValue {

    private final Class<?> type;
    private Object defaultValue;
    private Object value;

    public DefaultSettingsValue(Class<?> type) {
        this.type = type;
    }

    public DefaultSettingsValue(Class<?> type, Object value) {
        this.type = type;
        this.value = value;
    }

    public DefaultSettingsValue(Class<?> type, Object value, Object defaultValue) {
        this.type = type;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    public DefaultSettingsValue(SettingsValue value) {
        this.type = value.type();
        this.defaultValue = value.defaultValue();
        this.value = value.value();
    }

    @Override
    public Class<?> type() {
        return type;
    }

    @Override
    public Object value() {
        return value != null ? value : defaultValue;
    }

    @Override
    public void value(Object value) {
        this.value = castToThis(value);
    }

    @Override
    public Object defaultValue() {
        return this.defaultValue;
    }

    @Override
    public void defaultValue(Object defaultValue) {
        this.defaultValue = castToThis(defaultValue);
    }

    @Override
    public boolean isValuePresent() {
        return value != null || defaultValue != null;
    }

    private Object castToThis(Object value) {
        if (type.isAssignableFrom(value.getClass())) return value;

        if (value instanceof Number) {
            var num = (Number) value;
            if (type == int.class || type == Integer.class) {
                return num.intValue();
            } else if (type == long.class || type == Long.class) {
                return num.longValue();
            } else if (type == float.class || type == Float.class) {
                return num.floatValue();
            } else if (type == double.class || type == Double.class) {
                return num.doubleValue();
            } else if (type == byte.class || type == Byte.class) {
                return num.byteValue();
            } else if (type == short.class || type == Short.class) {
                return num.byteValue();
            }
        } else if (value instanceof Boolean) {
            return Boolean.TRUE.equals(value);
        }

        return type.cast(value);
    }
}
