package com.jason.permissionlib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 4:46 PM
 * desc   : 授权成功后 注解
 * version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnPermissionGranted {
    int value();
}
