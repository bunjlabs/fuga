package com.bunjlabs.fuga.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public abstract class AnnotationUtils {

    public static <A extends Annotation> A findAnnotation(AnnotatedElement source, Class<A> annotationType) {
        if (annotationType == null) return null;

        Annotation[] annotations = source.getDeclaredAnnotations();

        if (annotations.length != 0) {

        }
    }

    @SuppressWarnings("unchecked")
    static <A extends Annotation> A getDeclaredAnnotation(AnnotatedElement source, Class<A> annotationType) {
        Annotation[] annotations = getDeclaredAnnotations(source);
        for (Annotation annotation : annotations) {
            if (annotation != null && annotationType == annotation.annotationType()) {
                return (A) annotation;
            }
        }
        return null;
    }

    private static Annotation[] getDeclaredAnnotations(AnnotatedElement source) {
        Annotation[] annotations = source.getDeclaredAnnotations();

        if (annotations.length != 0) {
            return annotations;
        }

        return annotations.clone();
    }

    private static boolean hasPlainJavaAnnotationsOnly()
}
