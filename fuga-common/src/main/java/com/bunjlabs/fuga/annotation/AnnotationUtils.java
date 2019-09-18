package com.bunjlabs.fuga.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AnnotationUtils {

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

    @SuppressWarnings("unchecked")
    private static <A extends Annotation> A findAnnotation(Annotation[] annotations, Class<A> annotationType) {
        for (Annotation annotation : annotations) {
            if (annotation != null && annotationType == annotation.annotationType()) {
                return (A) annotation;
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
