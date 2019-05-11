package com.jason.permissionlib.helperImpl;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.jason.permissionlib.rationaleDialog.RationaleDialogFragmentCompat;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 10:49 AM
 * desc   :
 * version: 1.0
 */
public abstract class BaseSupportPermissionsHelper<T> extends PermissionHelper<T> {
    private static final String TAG = "BaseSupportPermissionsHelper";

    public BaseSupportPermissionsHelper(T mHost) {
        super(mHost);
    }
    public abstract FragmentManager getSupportFragmentManager();

    @SuppressLint("LongLogTag")
    @Override
    public void showRequestPermissionRationale(@NonNull String rationale, @NonNull String positiveButton, @NonNull String negativeButton, int theme, int requestCode, @NonNull String... perms) {
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentByTag(RationaleDialogFragmentCompat.TAG);
        if (fragment instanceof RationaleDialogFragmentCompat) {
            Log.d(TAG, "已存在请求 Fragment 弹框");
            return;
        }

        RationaleDialogFragmentCompat
                .newInstance(rationale, positiveButton, negativeButton, theme, requestCode, perms)
                .showAllowingStateLoss(fm, RationaleDialogFragmentCompat.TAG);

    }
}
