package com.warehouse.auth;

import com.warehouse.commonassets.enumeration.AccountLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mcp {

    McpModule module();

    boolean write() default false;

    AccountLevel minimumLevel() default AccountLevel.USER;
}
