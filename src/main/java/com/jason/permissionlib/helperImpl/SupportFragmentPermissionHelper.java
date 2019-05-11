package com.jason.permissionlib.helperImpl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 10:53 AM
 * desc   : 支持Fragment 的授权代理类
 * version: 1.0
 */
public class SupportFragmentPermissionHelper extends BaseSupportPermissionsHelper<Fragment> {

    public SupportFragmentPermissionHelper(@NonNull Fragment mHost) {
        super(mHost);
    }

    @Override
    public void directRequestPermissions(int requestCode, @NonNull String... perms) {
        getHost().requestPermissions(perms,requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String perm) {
        return getHost().shouldShowRequestPermissionRationale(perm);
    }

    @Override
    public Context getContext() {
        return getHost().getContext();
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return getHost().getChildFragmentManager();
    }
}
