package com.jason.permissionlib.rationaleDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;

import com.jason.permissionlib.interfaceImpl.IYZTPermissionCallback;
import com.jason.permissionlib.interfaceImpl.IYZTRationaleCallbacks;

/**
 * author : Jason
 * e-mail : jsonldrong@163.com
 * date   : 2019/4/29 2:18 PM
 * desc   : AppCompatDialogFragment 对话框
 * version: 1.0
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class RationaleDialogFragmentCompat extends AppCompatDialogFragment {
    public static final String TAG = "RationaleDialogFragmentCompat";
    private IYZTPermissionCallback mPermissionCallback; //权限回调
    private IYZTRationaleCallbacks mRationalCallback; //授权状态回调

    public static RationaleDialogFragmentCompat newInstance(
            @NonNull String rationaleMsg,
            @NonNull String positiveButton,
            @NonNull String negativeButton,
            @StyleRes int theme,
            int requestCode,
            @NonNull String[] permissions) {

        // 创建一个DialogFragment
        RationaleDialogFragmentCompat dialogFragment = new RationaleDialogFragmentCompat();

        //初始化DialogFragment
        RationaleDialogConfig config = new RationaleDialogConfig(
                positiveButton, negativeButton, rationaleMsg, theme, requestCode, permissions);
        dialogFragment.setArguments(config.toBundle());

        return dialogFragment;
    }

    /**
     * 展示授权对话框
     * @param manager FragmentManager
     * @param tag The tag for this fragment
     */
    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        if (manager.isStateSaved()) {
            return;
        }
        show(manager, tag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() != null) {
            if (getParentFragment() instanceof IYZTPermissionCallback) {
                mPermissionCallback = (IYZTPermissionCallback) getParentFragment();
            }
            if (getParentFragment() instanceof IYZTRationaleCallbacks){
                mRationalCallback = (IYZTRationaleCallbacks) getParentFragment();
            }
        }

        if (context instanceof IYZTPermissionCallback) {
            mPermissionCallback = (IYZTPermissionCallback) context;
        }

        if (context instanceof IYZTRationaleCallbacks) {
            mRationalCallback = (IYZTRationaleCallbacks) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPermissionCallback = null;
        mRationalCallback = null;
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
