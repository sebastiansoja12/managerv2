package com.warehouse.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessUserControl {

    String[] value() default {};

    String[] permissions() default {};

    boolean requireAllPermissions() default false;
}
