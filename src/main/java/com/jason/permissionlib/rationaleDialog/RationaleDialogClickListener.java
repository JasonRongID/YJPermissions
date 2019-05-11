package com.jason.permissionlib.rationaleDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import com.jason.permissionlib.helperImpl.PermissionHelper;
import com.jason.permissionlib.interfaceImpl.IYZTPermissionCallback;
import com.jason.permissionlib.interfaceImpl.IYZTRationaleCallbacks;

import java.util.Arrays;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 2:17 PM
 * desc   : 授权弹框的监听回调
 * version: 1.0
 */
public class RationaleDialogClickListener implements Dialog.OnClickListener {
    private Object mHost;
    private RationaleDialogConfig mConfig;
    private IYZTPermissionCallback mCallbacks; //权限回调
    private IYZTRationaleCallbacks mRationaleCallbacks; //授权状态回调

    /**
     * 针对AppCompatDialogFragment 监听回调
     *
     * @param compatDialogFragment AppCompatDialogFragment
     * @param config               RationaleDialog 配置
     * @param callbacks            权限监听回调
     * @param rationaleCallbacks   授权状态监听回调
     */
    RationaleDialogClickListener(RationaleDialogFragmentCompat compatDialogFragment,
                                 RationaleDialogConfig config,
                                 IYZTPermissionCallback callbacks,
                                 IYZTRationaleCallbacks rationaleCallbacks) {

        mHost = compatDialogFragment.getParentFragment() != null
                ? compatDialogFragment.getParentFragment()
                : compatDialogFragment.getActivity();

        mConfig = config;
        mCallbacks = callbacks;
        mRationaleCallbacks = rationaleCallbacks;

    }

    /**
     * 针对DialogFragment 监听回调
     *
     * @param dialogFragment DialogFragment
     * @param config         RationaleDialog 配置
     * @param callbacks      权限监听回调
     * @param dialogCallback 授权状态监听回调
     */
    RationaleDialogClickListener(RationaleDialogFragment dialogFragment,
                                 RationaleDialogConfig config,
                                 IYZTPermissionCallback callbacks,
                                 IYZTRationaleCallbacks dialogCallback) {

        mHost = dialogFragment.getActivity();

        mConfig = config;
        mCallbacks = callbacks;
        mRationaleCallbacks = dialogCallback;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int requestCode = mConfig.requestCode;
        if (which == Dialog.BUTTON_POSITIVE) {
            String[] permissions = mConfig.permissions;
            if (mRationaleCallbacks != null) {
                mRationaleCallbacks.onRationaleAccepted(requestCode);
            }
            if (mHost instanceof Activity) {
                PermissionHelper.newInstance((Activity) mHost).directRequestPermissions(requestCode, permissions);
            } else if (mHost instanceof Fragment) {
                PermissionHelper.newInstance((Fragment) mHost).directRequestPermissions(requestCode, permissions);
            } else {
                throw new RuntimeException("必须运行在Activity或者Fragment上！");
            }
        } else {
            if (mRationaleCallbacks != null){
                mRationaleCallbacks.onRationaleDenied(requestCode);
            }
            notifyPermissionDenied();
        }
    }

    /**
     * 通知权限授权被取消
     */
    private void notifyPermissionDenied() {
        if (mCallbacks != null) {
            mCallbacks.onPermissionsDenied(mConfig.requestCode, Arrays.asList(mConfig.permissions));

        }
    }
}
