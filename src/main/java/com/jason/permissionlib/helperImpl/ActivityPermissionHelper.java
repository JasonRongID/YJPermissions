package com.jason.permissionlib.helperImpl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.jason.permissionlib.rationaleDialog.RationaleDialogFragment;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 10:51 AM
 * desc   : Activity授权 代理类
 * version: 1.0
 */
public class ActivityPermissionHelper extends PermissionHelper<Activity> {
    private static final String TAG = "ActivityPermissionHelper";

    public ActivityPermissionHelper(Activity mHost) {
        super(mHost);
    }

    @Override
    public void directRequestPermissions(int requestCode, @NonNull String... perms) {
        ActivityCompat.requestPermissions(getHost(),perms,requestCode);
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String perm) {
        return ActivityCompat.shouldShowRequestPermissionRationale(getHost(),perm);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void showRequestPermissionRationale(@NonNull String rationale, @NonNull String positiveButton, @NonNull String negativeButton, int theme, int requestCode, @NonNull String... perms) {
        FragmentManager fm = getHost().getFragmentManager();
        Fragment fragment = fm.findFragmentByTag(RationaleDialogFragment.TAG);
        if (fragment instanceof RationaleDialogFragment) {
            Log.d(TAG, "已存在Fragment showing");
            return;
        }
        RationaleDialogFragment.newInstance(positiveButton,negativeButton,rationale,theme,requestCode,perms)
            .showAllowingStateLoss(fm,RationaleDialogFragment.TAG);
    }

    @Override
    public Context getContext() {
        return getHost();
    }
}
