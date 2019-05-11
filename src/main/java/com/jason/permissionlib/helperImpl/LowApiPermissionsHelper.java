package com.jason.permissionlib.helperImpl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 10:48 AM
 * desc   : 低版本授权代理类
 * version: 1.0
 */
public class LowApiPermissionsHelper<T> extends PermissionHelper<T> {
    public LowApiPermissionsHelper(@NonNull T mHost) {
        super(mHost);
    }

    @Override
    public void directRequestPermissions(int requestCode, @NonNull String... perms) {

    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String perm) {
        return false;
    }

    @Override
    public void showRequestPermissionRationale(@NonNull String rationale, @NonNull String positiveButton, @NonNull String negativeButton, int theme, int requestCode, @NonNull String... perms) {
        throw new IllegalStateException("API < 23");
    }

    @Override
    public Context getContext() {
        if (getHost() instanceof Activity){
            return (Context) getHost();
        }else if (getHost() instanceof Fragment){
            return ((Fragment) getHost()).getContext();
        }else {
            throw new IllegalStateException("Context 参数错误");
        }
    }
}
