package com.jason.permissionlib.rationaleDialog;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.jason.permissionlib.interfaceImpl.IYZTPermissionCallback;
import com.jason.permissionlib.interfaceImpl.IYZTRationaleCallbacks;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 1:47 PM
 * desc   : 权限申请 对话框
 * version: 1.0
 */
public class RationaleDialogFragment extends DialogFragment {
    public static final String TAG = "RationaleDialogFragment";
    private IYZTPermissionCallback mPermissionCallback; //权限回调
    private IYZTRationaleCallbacks mRationalCallback; //授权状态回调
    private boolean mStateSaved = false;


    public static RationaleDialogFragment newInstance(
            @NonNull String positiveButton,
            @NonNull String negativeButton,
            @NonNull String rationaleMsg,
            @StyleRes int theme,
            int requestCode,
            @NonNull String[] permissions) {

        //创建一个dialogFragment
        RationaleDialogFragment dialogFragment = new RationaleDialogFragment();

        // 初始化RationaleDialogConfig 数据
        RationaleDialogConfig config = new RationaleDialogConfig(
                positiveButton, negativeButton, rationaleMsg, theme, requestCode, permissions);
        dialogFragment.setArguments(config.toBundle());

        return dialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && getParentFragment() != null) {
            if (getParentFragment() instanceof IYZTPermissionCallback) {
                mPermissionCallback = (IYZTPermissionCallback) getParentFragment();
            }
            if (getParentFragment() instanceof IYZTRationaleCallbacks) {
                mRationalCallback = (IYZTRationaleCallbacks) getParentFragment();
            }
        }
        if (context instanceof IYZTPermissionCallback){
            mPermissionCallback = (IYZTPermissionCallback) context;
        }
        if (context instanceof  IYZTRationaleCallbacks){
            mRationalCallback = (IYZTRationaleCallbacks) context;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mStateSaved = true;
        super.onSaveInstanceState(outState);
    }


    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        // API 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.isStateSaved()) {
                return;
            }
        }

        if (mStateSaved) {
            return;
        }

        show(manager, tag);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPermissionCallback = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 设置授权弹框 不可被取消
        setCancelable(false);

        // 从参数只能够获取 Config 创建授权弹框的监听回调
        RationaleDialogConfig config = new RationaleDialogConfig(getArguments());
        RationaleDialogClickListener clickListener =
                new RationaleDialogClickListener(this, config, mPermissionCallback, mRationalCallback);

        //创建授权对话框
        return config.createFrameworkDialog(getActivity(), clickListener);
    }
}
