package com.bunjlabs.fuga.annotation;

import java.lang.annotation.Annotation;

public interface AnnotationFilter {

    AnnotationFilter PLAIN = packages("java.lang", "com.bunjlabs.fuga");
    AnnotationFilter FUGA = packages("com.bunjlabs.fuga");
    AnnotationFilter JAVA = packages("java", "javax");

    static AnnotationFilter packages(String... packages) {
        return new PrefixesAnnotationFilter(packages);
    }

    default boolean matches(Annotation annotation) {
        return matches(annotation.annotationType());
    }

    default boolean matches(Class<?> type) {
        return matches(type.getName());
    }

    boolean matches(String annotationTypeName);
}
