package com.mpdev.reporting.transformation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;

@Retention(RUNTIME)
@Target({METHOD})
public @interface Transformation {
    String fieldName();
    String contractType() default "*";
    String[] jurisdiction() default {"*"};
}
