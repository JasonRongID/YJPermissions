package com.jason.permissionlib;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.jason.permissionlib.helperImpl.PermissionHelper;
import com.jason.permissionlib.interfaceImpl.IYZTPermissionCallback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/299:49 AM
 * desc   : 动态权限类
 * version: 1.0
 */
public class YZTPermissions {
    private static final String TAG = "YZTPermissions";
    /**
     * @param context 权限申请的上下文
     * @param permissions 需要申请的权限
     * @return 是否授权
     */
    public static boolean hasPermissions(@NonNull Context context, @NonNull String... permissions){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Log.d(TAG,"版本号小于23，走默认权限配置");
            return true;
        }
        if (context == null){
            throw new IllegalArgumentException("权限申请的上下文不能为空");
        }
        for (String permission: permissions){
            if (PermissionAssembles.permissionExists(permission)&&ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 请求授权Activity上下文
     * @param activity  activity
     * @param rationale 描述
     * @param requestCode 权限请求Code
     * @param permission 需要授权的权限
     */
    public static void requestPermissions(@NonNull Activity activity,@NonNull String rationale,
                                   @NonNull int requestCode,@Size(min = 1) @NonNull String... permission){
        requestPermission(new PermissionRequest.Builder(activity,requestCode,permission).setRationale(rationale).builder());
    }

    /**
     * 请求授权Fragment上下文
     * @param fragment  fragment
     * @param rationale 描述
     * @param requestCode 权限请求Code
     * @param permission 需要授权的权限
     */
    public static void requestPermissions(@NonNull Fragment fragment, @NonNull String rationale,
                                   @NonNull int requestCode, @Size(min = 1) @NonNull String... permission){
        requestPermission(new PermissionRequest.Builder(fragment,requestCode,permission).setRationale(rationale).builder());
    }

    /**
     *请求权限
     * @param request permissionRequest构建类
     */
    public static void requestPermission(PermissionRequest request){
        if (hasPermissions(request.getHelper().getContext(),request.getPerms())){
            notifyAlreadyHasPermissions(request.getHelper().getHost(),request.getRequestCode(),request.getPerms());
            return;
        }
        request.getHelper().requestPermissions(
                request.getRationale(),
                request.getPositiveButtonText(),
                request.getNegativeButtonText(),
                request.getTheme(),
                request.getRequestCode(),
                request.getPerms()
        );
    }

    /**
     * 对请求权限但已经拥有权限的对象 权限回调
     * @param object
     * @param requestCode
     * @param permissions
     */
    private static void notifyAlreadyHasPermissions(@NonNull Object object,int requestCode,@NonNull String... permissions){
        int[] grantResults = new int[permissions.length];
        for (int i= 0;i < grantResults.length;i++){
            grantResults[i] = PackageManager.PERMISSION_GRANTED;
        }
        onRequestPermissionsResult(requestCode, permissions, grantResults, object);
    }

    /**
     * 请求权限授权 结果回调
     * @param requestCode 权限授权请求码
     * @param permissions 权限
     * @param grantResults 授权结果
     * @param receivers 具有注释方法的对象数组
     */
    public static void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults, @NonNull Object... receivers) {
        //创建两个集合存储所有的请求
        List<String> granted = new ArrayList<>();
        List<String> denied = new ArrayList<>();
        for (int i=0;i< permissions.length;i++){
            String prem = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                granted.add(prem);
            }else {
                denied.add(prem);
            }
        }
        //迭代所以的具有注释的方法对象
        for (Object object: receivers){
            if (!granted.isEmpty()){
                if (object instanceof IYZTPermissionCallback){
                    ((IYZTPermissionCallback) object).onPermissionsGranted(requestCode,granted);
                }
            }
            if (!denied.isEmpty()){
                if (object instanceof IYZTPermissionCallback){
                    ((IYZTPermissionCallback) object).onPermissionsDenied(requestCode,granted);
                }
            }
            //如果 请调用带注释的方法
            if (!granted.isEmpty()&&denied.isEmpty()){
                runAnnotatedMethods(object, requestCode);
            }
        }
    }

    /**
     * 检查是否已永久拒绝拒绝权限列表中的至少一个权限（用户单击“从不再询问”）。
     * @param host 上下文Activity
     * @param deniedPermissions 拒绝的权限
     * @return 返回 检查是否已永久拒绝拒绝权限列表中的至少一个权限
     */
    public static boolean somePermissionPermanentlyDenied(@NonNull Activity host,
                                                       @NonNull List<String> deniedPermissions){
        return PermissionHelper.newInstance(host).somePermissionPermanentlyDenied(deniedPermissions);
    }

    /**
     * 检查是否已永久拒绝拒绝权限列表中的至少一个权限（用户单击“从不再询问”）。
     * @param host 上下文Fragment
     * @param deniedPermissions 拒绝的权限
     * @return 返回 检查是否已永久拒绝拒绝权限列表中的至少一个权限
     */
    public static boolean somePermissionPermanentlyDenied(@NonNull Fragment host,
                                                          @NonNull List<String> deniedPermissions){
        return PermissionHelper.newInstance(host).somePermissionPermanentlyDenied(deniedPermissions);
    }

    /**
     * 检查是否已永久拒绝权限（用户单击“从不再询问”）。
     * @param host 上下文Activity
     * @param deniedPermission 拒绝的权限
     * @return
     */
    public static boolean permissionPermanentlyDenied(@NonNull Activity host,@NonNull String deniedPermission){
        return PermissionHelper.newInstance(host).permissionPermanentlyDenied(deniedPermission);
    }
    /**
     * 检查是否已永久拒绝权限（用户单击“从不再询问”）。
     * @param host 上下文Fragment
     * @param deniedPermission 拒绝的权限
     * @return
     */
    public static boolean permissionPermanentlyDenied(@NonNull Fragment host,@NonNull String deniedPermission){
        return PermissionHelper.newInstance(host).permissionPermanentlyDenied(deniedPermission);
    }

    /**
     * 看看是否有一些被拒绝的许可被永久拒绝。
     * @param host 上下文 Activity
     * @param perms 权限列表
     * @return
     */
    public static boolean somePermissionDenied(@NonNull Activity host,@NonNull String... perms){
        return PermissionHelper.newInstance(host).somePermissionDenied(perms);
    }
    /**
     * 看看是否有一些被拒绝的许可被永久拒绝。
     * @param host 上下文 Fragment
     * @param perms 权限列表
     * @return
     */
    public static boolean somePermissionDenied(@NonNull Fragment host,@NonNull String... perms){
        return PermissionHelper.newInstance(host).somePermissionDenied(perms);
    }

    /**
     * 运行具有的注解方法
     * @param object  添加注释的方法
     * @param requestCode 权限请求码
     */
    private static void runAnnotatedMethods(@NonNull Object object, int requestCode) {
        Class clazz = object.getClass();
        if (isUsingAndroidAnnotations(object)){
            clazz = clazz.getSuperclass();
        }
        while (clazz != null){
            for (Method method:clazz.getDeclaredMethods()){
                OnPermissionGranted annotation = method.getAnnotation(OnPermissionGranted.class);
                if (annotation != null){
                    //判断注解里的value是否等于 权限请求码
                    if (requestCode == annotation.value()){
                        //方法类型一定要是void 否则反射报错
                        if (method.getParameterTypes().length > 0){
                            throw new RuntimeException("方法类型一定要是void 否则反射报错");
                        }

                        try {
                            //如果方法是私有的
                            if (!method.isAccessible()) {
                                method.setAccessible(true);
                            }
                            method.invoke(object);
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
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 确定项目是否使用AndroidAnnotations库
     * @param object 添加注释的方法
     * @return 是否使用AndroidAnnotations库
     */
    private static boolean isUsingAndroidAnnotations(Object object) {
        if (!object.getClass().getSimpleName().endsWith("_")) {
            return false;
        }
        try {
            Class clazz = Class.forName("org.androidannotations.api.view.HasViews");
            return clazz.isInstance(object);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
