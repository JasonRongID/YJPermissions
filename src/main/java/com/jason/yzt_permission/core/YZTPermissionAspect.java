package com.jason.yzt_permission.core;


import android.content.Context;
import android.util.ArrayMap;
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
    private ArrayMap<Integer,Object[]> cachePermissionMap = new ArrayMap<>();

    @Pointcut("execution(@com.jason.yzt_permission.annotation.Permission * *(..)) && @annotation(permission)")
    public void requestPermission(Permission permission) {

    }

    @Around("requestPermission(permission)")
    public synchronized void aroundJointPoint(final ProceedingJoinPoint joinPoint, final Permission permission) throws Throwable{

        //初始化context
        Context context = null;
        //特殊场景如内部类中 实现需要传内部类class
        Class<?> clz = null;

        final Object object = joinPoint.getThis();
        if (joinPoint.getThis() instanceof Context) {
            context = (Context) object;
        } else if (joinPoint.getThis() instanceof android.support.v4.app.Fragment) {
            context = ((android.support.v4.app.Fragment) object).getActivity();
        } else if (joinPoint.getThis() instanceof android.app.Fragment) {
            context = ((android.app.Fragment) object).getActivity();
        } else {
            Object[] args = joinPoint.getArgs();
            if (args != null&&args.length>0){
                for (int i=0;i<args.length;i++){
                    if (args[i] instanceof Context){
                        context = (Context) args[i];
                    }
                    if (args[i] instanceof Class<?>){
                        clz = (Class<?>) args[i];
                    }
                }
            }
        }

        if (context == null || permission == null) {
            Log.d(TAG, "aroundJonitPoint error ");
            return;
        }
        cachePermissionMap.put(permission.requestCode(),joinPoint.getArgs());

        synchronized (this){
            final Class<?> finalClz = clz;
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
                    Object[] params = cachePermissionMap.get(requestCode);
                    cachePermissionMap.remove(requestCode);
                    if (finalClz != null){
                        PermissionUtils.invokAnnotationSpecial(object,finalClz, PermissionCanceled.class,requestCode,params);
                    }else {
                        PermissionUtils.invokAnnotation(object, PermissionCanceled.class,requestCode,params);
                    }
                }

                @Override
                public void denied(int requestCode) {
                    Object[] params = cachePermissionMap.get(requestCode);
                    cachePermissionMap.remove(requestCode);
                    if (finalClz != null){ //特殊处理
                        PermissionUtils.invokAnnotationSpecial(object,finalClz,PermissionDenied.class,requestCode,params);
                    }else{
                        PermissionUtils.invokAnnotation(object, PermissionDenied.class,requestCode, params);
                    }
                }

            });
        }

    }



}
