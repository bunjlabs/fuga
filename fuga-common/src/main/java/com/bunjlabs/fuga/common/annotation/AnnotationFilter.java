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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

public interface AnnotationFilter {

    AnnotationFilter ALL = annotationTypeName -> true;
    AnnotationFilter PLAIN = packages("java.lang");
    AnnotationFilter FUGA = packages("com.bunjlabs.fuga");
    AnnotationFilter JAVA = packages("java", "javax");

    static AnnotationFilter packages(String... packages) {
        return new PrefixesAnnotationFilter(packages);
    }

    default boolean matches(Annotation annotation) {
        return matches(annotation.annotationType());
    }

    default boolean matches(AnnotatedElement annotatedElement) {
        if (annotatedElement instanceof Class) {
            return matches((Class<?>) annotatedElement);
        } else if (annotatedElement instanceof Member) {
            return matches(((Member) annotatedElement).getDeclaringClass());
        } else {
            return false;
        }
    }

    default boolean matches(Class<?> type) {
        return matches(type.getName());
    }

    boolean matches(String annotationTypeName);
}
