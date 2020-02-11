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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AnnotationUtils {

    public static boolean hasAnnotation(AnnotatedElement source, Class<? extends Annotation> annotationType) {
        return findAnnotation(source, annotationType) != null;
    }

    public static boolean hasAnnotation(Annotation[] annotations, Class<? extends Annotation> annotationType) {
        return findAnnotation(annotations, annotationType) != null;
    }

    public static <A extends Annotation> A findAnnotation(AnnotatedElement source, Class<A> annotationType) {
        return findAnnotation(source, annotationType, AnnotationFilter.ALL);
    }

    public static <A extends Annotation> A findAnnotation(AnnotatedElement source, Class<A> annotationType, AnnotationFilter annotationFilter) {
        if (annotationType == null) return null;

        if (AnnotationFilter.PLAIN.matches(annotationType)) {
            return source.getDeclaredAnnotation(annotationType);
        }

        Annotation[] annotations = getDeclaredAnnotations(source, annotationFilter);

        if (annotations.length == 0) {
            return null;
        }

        A annotation = findAnnotation(annotations, annotationType);

        if (annotation != null) {
            return annotation;
        }

        for (Annotation metaAnnotation : annotations) {
            if (AnnotationFilter.PLAIN.matches(metaAnnotation)) continue;

            annotation = findAnnotation(metaAnnotation.annotationType(), annotationType, annotationFilter);

            if (annotation != null) return annotation;
        }

        return null;
    }

    public static Set<Annotation> getAllAnnotations(AnnotatedElement source) {
        return getAllAnnotations(source, AnnotationFilter.ALL);
    }

    public static Set<Annotation> getAllAnnotations(AnnotatedElement source, AnnotationFilter annotationFilter) {
        Annotation[] annotations = getDeclaredAnnotations(source, annotationFilter);

        if (annotations.length == 0) {
            return Collections.emptySet();
        }

        return Arrays.stream(annotations).filter(annotationFilter::matches).collect(Collectors.toUnmodifiableSet());
    }

    public static boolean isRetainedAtRuntime(Class<? extends Annotation> annotationType) {
        Retention retention = annotationType.getAnnotation(Retention.class);
        return retention != null && retention.value() == RetentionPolicy.RUNTIME;
    }

    public static <A extends Annotation> A findAnnotation(Annotation[] annotations, Class<A> annotationType) {
        for (Annotation annotation : annotations) {
            if (annotation != null && annotationType == annotation.annotationType()) {
                @SuppressWarnings("unchecked")
                var result = (A) annotation;
                return result;
            }
        }
        return null;
    }

    private static Annotation[] getDeclaredAnnotations(AnnotatedElement source, AnnotationFilter annotationFilter) {
        Annotation[] annotations = source.getDeclaredAnnotations();

        if (annotations.length != 0) {
            return Stream.of(annotations)
                    .filter(annotationFilter::matches)
                    .collect(Collectors.toUnmodifiableList())
                    .toArray(new Annotation[]{});
        }

        return annotations.clone();
    }
}
