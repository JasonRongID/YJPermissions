package com.jason.permissionlib.helperImpl;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 10:20 AM
 * desc   : 权限代理实现类
 * version: 1.0
 */
public abstract class PermissionHelper<T> {
    private T mHost; //基础

    public PermissionHelper(T mHost) {
        this.mHost = mHost;
    }

    /**
     * Activity 代理类
     * @param mHost 请求类
     * @return 相应的代理类
     */
    @NonNull
    public static PermissionHelper<? extends Activity> newInstance(Activity mHost){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return new LowApiPermissionsHelper<>(mHost);
        }
        if (mHost instanceof AppCompatActivity){
            return new AppCompatActivityPermissionsHelper((AppCompatActivity) mHost);
        }else {
            return new ActivityPermissionHelper(mHost);
        }
    }

    /**
     * Fragment 代理类
     * @param mHost 请求类
     * @return 相应的代理类
     */
    @NonNull
    public static PermissionHelper<Fragment> newInstance(Fragment mHost){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return new LowApiPermissionsHelper<>(mHost);
        }
        return new SupportFragmentPermissionHelper(mHost);
    }

    /**
     *
     * @param prems 权限名称
     * @return 是否需要 权限解释
     */
    private boolean shouldShowRationale(@NonNull String... prems){
        for (String prem : prems){
            if (shouldShowRequestPermissionRationale(prem)){
                return true;
            }
        }
        return false;
    }

    /**
     * 请求权限授权
     * @param rationale 权限解释
     * @param positiveButton 同意按钮文案
     * @param negativeButton 拒绝按钮文案
     * @param theme 弹框样式
     * @param requestCode 请求Code
     * @param perms 请求的权限
     */
    public void requestPermissions(@NonNull String rationale,
                                   @NonNull String positiveButton,
                                   @NonNull String negativeButton,
                                   @StyleRes int theme,
                                   int requestCode,
                                   @NonNull String... perms){
        if (shouldShowRationale(perms)){
            showRequestPermissionRationale(rationale,positiveButton,negativeButton,theme,requestCode,perms);
        }else {
            directRequestPermissions(requestCode,perms);
        }
    }

    /**
     * 不再提醒
     * @param perms 权限请求
     * @return
     */
    public boolean somePermissionPermanentlyDenied(@NonNull List<String> perms) {
        for (String deniedPermission : perms) {
            if (permissionPermanentlyDenied(deniedPermission)) {
                return true;
            }
        }

        return false;
    }

    public boolean permissionPermanentlyDenied(@NonNull String perms) {
        return !shouldShowRequestPermissionRationale(perms);
    }

    public boolean somePermissionDenied(@NonNull String... perms) {
        return shouldShowRationale(perms);
    }
    @NonNull
    public T getHost() {
        return mHost;
    }
    /**
     * 直接请求权限
     * @param requestCode 请求码
     * @param perms 请求的权限
     */
    public abstract void directRequestPermissions(int requestCode, @NonNull String... perms);

    /**
     * @param perm 需要授权的权限
     * @return 是否需要授权
     */
    public abstract boolean shouldShowRequestPermissionRationale(@NonNull String perm);

    /**
     * @param rationale 授权解释
     * @param positiveButton
     * @param negativeButton
     * @param theme
     * @param requestCode
     * @param perms
     */
    public abstract void showRequestPermissionRationale(@NonNull String rationale,
                                                        @NonNull String positiveButton,
                                                        @NonNull String negativeButton,
                                                        @StyleRes int theme,
                                                        int requestCode,
                                                        @NonNull String... perms);
    public abstract Context getContext();

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
