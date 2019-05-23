package com.jason.yzt_permission;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import android.util.Log;


import com.jason.yzt_permission.annotation.PermissionCanceled;
import com.jason.yzt_permission.annotation.PermissionDenied;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

public class PermissionUtils {
    private static final String TAG = "PermissionUtils";
    public static final int DEFAULT_REQUEST_CODE = 0xABC1994;

    private static SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23);
    }

    /**
     * 检查是否需要请求权限
     * @param context
     * @param permissions
     * @return
     *      false --- 需要  true ---不需要
     */
    public static boolean hasPermission(Context context, String ...permissions) {

        for (String permission : permissions) {
            if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Description 检测某个权限是否已经授权；如果已授权则返回true，如果未授权则返回false
     * @version
     */
    private static boolean hasSelfPermission(Context context, String permission) {
        try {
            // ContextCompat.checkSelfPermission，主要用于检测某个权限是否已经被授予。
            // 方法返回值为PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED
            // 当返回DENIED就需要进行申请授权了。
            return ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return false;
        }
    }

    /**
     * @Description 如果在这个SDK版本存在的权限，则返回true
     * @version
     */
    private static boolean permissionExists(String permission) {
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion;
    }

    public static boolean verifyPermission(Context context, int ... gantedResults) {

        if (gantedResults == null || gantedResults.length == 0 ) {
            return false;
        }

        for (int ganted : gantedResults) {
            if (ganted != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Description 检查需要给予的权限是否需要显示理由
     * @version
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            // 这个API主要用于给用户一个申请权限的解释，该方法只有在用户在上一次已经拒绝过你的这个权限申请。
            // 也就是说，用户已经拒绝一次了，你又弹个授权框，你需要给用户一个解释，为什么要授权，则使用该方法。
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 常见的方法
     * @param object 上下文
     * @param annotationClass 注解Class
     * @param requestCode 请求码
     * @param params 请求参数
     */
    public static void invokAnnotation(Object object, Class annotationClass,int requestCode,Object... params) {

        //获取切面上下文的类型
        Class<?> clz = object.getClass();
        //获取类型中的方法
        Method[] methods = clz.getDeclaredMethods();
        if (methods == null) {
            return;
        }
        invokePermissionMethod(object, annotationClass, requestCode, methods, params);
    }

    /**
     * 特殊的方法 针对内部类
     * @param object 切面上下文
     * @param clz 内部类class
     * @param annotationClass 注解class
     * @param requestCode 请求码
     * @param params 请求参数
     */
    public static void invokAnnotationSpecial(Object object,Class<?> clz,Class annotationClass,int requestCode,Object... params) {

        //获取切面上下文的类型
        if (clz == null) return;
        //获取类型中的方法
        Method[] methods = clz.getDeclaredMethods();
        if (methods == null) return;
        invokePermissionMethod(object, annotationClass, requestCode, methods, params);
    }

    /**
     * 反射含有注解的方法
     * @param object 上下文
     * @param annotationClass 注解Class
     * @param requestCode 请求码
     * @param methods 找到的含有注解的方法
     * @param params 请求参数
     */
    private static void invokePermissionMethod(Object object, Class annotationClass, int requestCode, Method[] methods, Object[] params) {
        for (Method method : methods) {
            if (annotationClass == PermissionCanceled.class){
                PermissionCanceled annotation = method.getAnnotation(PermissionCanceled.class);
                if (annotation != null){
                    //判断注解里的value是否等于 权限请求码
                    if (requestCode == annotation.requestCode()){
                        checkMethodVoid(method);
                        try {
                            //如果方法是私有的
                            if (!method.isAccessible()) {
                                method.setAccessible(true);
                            }
                            if (null ==params||params.length <=0){
                                method.invoke(object);
                            }else {
                                method.invoke(object,params);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.getMessage());
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.getMessage());
                        }
                    }
                }
            }else if (annotationClass == PermissionDenied.class){
                PermissionDenied annotation = method.getAnnotation(PermissionDenied.class);
                if (annotation != null){
                    //判断注解里的value是否等于 权限请求码
                    if (requestCode == annotation.requestCode()){
                        checkMethodVoid(method);
                        try {
                            //如果方法是私有的
                            if (!method.isAccessible()) {
                                method.setAccessible(true);
                            }
                            if (null ==params||params.length <= 0){
                                method.invoke(object);
                            }else {
                                method.invoke(object,params);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.getMessage());
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.getMessage());
                        }
                    }
                }
            }
        }
    }

    /**
     * 检查方法是否是void
     * @param method 需要反射的方法
     */
    private static void checkMethodVoid(Method method) {
        //方法类型一定要是void 否则反射报错
        Type genericReturntype = method.getGenericReturnType();
        if (genericReturntype instanceof ParameterizedType){
            Type[] actualTypeArguments = ((ParameterizedType) genericReturntype).getActualTypeArguments();
            if (actualTypeArguments.length>0){
                throw new RuntimeException("方法类型一定要是void 否则反射报错");
            }
        }
    }
}
