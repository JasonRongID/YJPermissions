package com.jason.yzt_permission.core;


import android.content.Context;
import android.util.Log;


import com.jason.yzt_permission.PermissionUtils;
import com.jason.yzt_permission.YZTPermissionActivity;
import com.jason.yzt_permission.annotation.Permission;
import com.jason.yzt_permission.annotation.PermissionCanceled;
import com.jason.yzt_permission.annotation.PermissionDenied;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class YZTPermissionAspect {

    private static final String TAG = "YZTPermissionAspect";

    @Pointcut("execution(@com.jason.yzt_permission.annotation.Permission * *(..)) && @annotation(permission)")
    public void requestPermission(Permission permission) {

    }

    @Around("requestPermission(permission)")
    public void aroundJointPoint(final ProceedingJoinPoint joinPoint, final Permission permission) throws Throwable{

        //初始化context
        Context context = null;

        final Object object = joinPoint.getThis();
        if (joinPoint.getThis() instanceof Context) {
            context = (Context) object;
        } else if (joinPoint.getThis() instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) object).getActivity();
        } else if (joinPoint.getThis() instanceof android.app.Fragment) {
            context = ((android.app.Fragment) object).getActivity();
        } else {
        }

        if (context == null || permission == null) {
            Log.d(TAG, "aroundJonitPoint error ");
            return;
        }

        final Context finalContext = context;
        YZTPermissionActivity.requestPermission(context, permission.value(), permission.requestCode(), new IPermission() {
            @Override
            public void ganted(int requestCode) {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
            }

            @Override
            public void cancled(int requestCode) {
                    PermissionUtils.invokAnnotation(object, PermissionCanceled.class,requestCode);
            }

            @Override
            public void denied(int requestCode) {
                    PermissionUtils.invokAnnotation(object, PermissionDenied.class,requestCode);
            }

        });

    }



}
