package com.jason.yzt_permission.annotation;


import com.jason.yzt_permission.PermissionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionDenied {
    int requestCode() default PermissionUtils.DEFAULT_REQUEST_CODE;
}
