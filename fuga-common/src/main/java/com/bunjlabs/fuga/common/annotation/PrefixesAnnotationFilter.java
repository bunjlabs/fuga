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

package com.bunjlabs.fuga.common.annotation;

import com.bunjlabs.fuga.util.Assert;
import com.bunjlabs.fuga.util.ObjectUtils;

import java.util.Arrays;

public class PrefixesAnnotationFilter implements AnnotationFilter {
    private final String[] prefixes;
    private final int hashCode;

    public PrefixesAnnotationFilter(String[] prefixes) {
        Assert.notNull(prefixes, "Prefixes array must not be null");
        this.prefixes = new String[prefixes.length];
        for (int i = 0; i < prefixes.length; i++) {
            this.prefixes[i] = Assert.hasText(prefixes[i], "Prefixes array must not have empty elements");
        }
        Arrays.sort(this.prefixes);
        this.hashCode = Arrays.hashCode(this.prefixes);
    }

    @Override
    public boolean matches(String annotationTypeName) {
        for (String prefix : this.prefixes) {
            if (annotationTypeName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrefixesAnnotationFilter f = (PrefixesAnnotationFilter) o;
        return Arrays.equals(prefixes, f.prefixes);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return ObjectUtils.toStringJoiner(PrefixesAnnotationFilter.class)
                .add("prefixes", String.join(", ", prefixes))
                .toString();
    }
}
