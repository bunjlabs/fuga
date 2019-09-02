package com.bunjlabs.fuga.annotation;

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
